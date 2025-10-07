---
applyTo: "**/domain/**/*.ts,**/entities/**/*.ts,**/value-objects/**/*.ts"
description: "Padrões para Domain-Driven Design com agregados, entidades e value objects"
---

# Instruções de Modelagem de Domínio (DDD)

## Objetivo
Estabelecer padrões consistentes para implementação de Domain-Driven Design em TypeScript/NestJS, garantindo modelos ricos em comportamento e alinhados com o negócio.

## Escopo
- Modelagem estratégica (Bounded Contexts, Context Mapping)
- Modelagem tática (Entities, Value Objects, Aggregates)
- Domain Services e Domain Events
- Ubiquitous Language e Anti-corruption Layer
- Repository Pattern e Specifications

## Pessoal vs Enterprise

### Projetos Pessoais (Foco: Simplicidade)
- Agregados simples
- Eventos de domínio básicos
- Repository pattern direto
- Validações básicas no domínio

### Projetos Enterprise (Foco: Robustez)
- Bounded contexts bem definidos
- Context mapping documentado
- Domain events com saga patterns
- CQRS quando apropriado
- Specifications para queries complexas
- Anti-corruption layers para integrações

## Domain-Driven Design Estratégico

### Bounded Context Definition
```typescript
// domain/contexts/user-management/user-management.context.ts
/**
 * User Management Bounded Context
 * 
 * Responsabilidades:
 * - Gerenciamento de usuários e perfis
 * - Autenticação e autorização
 * - Preferências de usuário
 * 
 * Não inclui:
 * - Cobrança/Payment (Payment Context)
 * - Notificações (Notification Context)
 */
export namespace UserManagementContext {
  export interface IUserRepository {
    save(user: User): Promise<void>;
    findById(id: UserId): Promise<User | null>;
    findByEmail(email: Email): Promise<User | null>;
  }

  export interface IDomainEventPublisher {
    publish<T extends DomainEvent>(event: T): Promise<void>;
  }
}
```

### Context Map Documentation
```typescript
// docs/context-map.ts
/**
 * Context Mapping entre bounded contexts
 * 
 * User Management -> Payment (Customer/Supplier)
 * - User Management fornece informações de usuário
 * - Payment consome via Anti-corruption Layer
 * 
 * User Management -> Notification (Publisher/Subscriber)
 * - User Management publica eventos de usuário
 * - Notification consome eventos
 */
export const CONTEXT_RELATIONSHIPS = {
  USER_TO_PAYMENT: 'Customer/Supplier',
  USER_TO_NOTIFICATION: 'Publisher/Subscriber',
  USER_TO_ANALYTICS: 'Open Host Service',
} as const;
```

## Modelagem Tática - Building Blocks

### 1. Value Objects
```typescript
// domain/value-objects/email.value-object.ts
export class Email {
  private static readonly EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  
  private constructor(private readonly _value: string) {
    this.validate();
  }
  
  static create(value: string): Email {
    if (!value || typeof value !== 'string') {
      throw new ValidationException('Email is required');
    }
    
    return new Email(value.toLowerCase().trim());
  }
  
  get value(): string {
    return this._value;
  }
  
  equals(other: Email): boolean {
    return this._value === other._value;
  }
  
  private validate(): void {
    if (!Email.EMAIL_REGEX.test(this._value)) {
      throw new ValidationException('Invalid email format');
    }
  }
}

// domain/value-objects/user-name.value-object.ts
export class UserName {
  private constructor(
    private readonly _firstName: string,
    private readonly _lastName: string
  ) {}
  
  static create(firstName: string, lastName: string): UserName {
    this.validateName(firstName, 'First name');
    this.validateName(lastName, 'Last name');
    
    return new UserName(
      firstName.trim(),
      lastName.trim()
    );
  }
  
  get fullName(): string {
    return `${this._firstName} ${this._lastName}`;
  }
  
  get firstName(): string {
    return this._firstName;
  }
  
  get lastName(): string {
    return this._lastName;
  }
  
  private static validateName(name: string, field: string): void {
    if (!name || name.trim().length < 2) {
      throw new ValidationException(`${field} must have at least 2 characters`);
    }
    
    if (name.trim().length > 50) {
      throw new ValidationException(`${field} cannot exceed 50 characters`);
    }
  }
}
```

### 2. Entities com Identity
```typescript
// domain/entities/user.entity.ts
export class User {
  private _domainEvents: DomainEvent[] = [];
  
  private constructor(
    private readonly _id: UserId,
    private _name: UserName,
    private _email: Email,
    private _status: UserStatus,
    private readonly _createdAt: Date,
    private _updatedAt: Date
  ) {}
  
  static create(props: {
    name: UserName;
    email: Email;
  }): User {
    const user = new User(
      UserId.generate(),
      props.name,
      props.email,
      UserStatus.ACTIVE,
      new Date(),
      new Date()
    );
    
    user.addDomainEvent(new UserCreatedEvent({
      userId: user.id,
      email: user.email.value,
      name: user.name.fullName
    }));
    
    return user;
  }
  
  static reconstitute(props: {
    id: UserId;
    name: UserName;
    email: Email;
    status: UserStatus;
    createdAt: Date;
    updatedAt: Date;
  }): User {
    return new User(
      props.id,
      props.name,
      props.email,
      props.status,
      props.createdAt,
      props.updatedAt
    );
  }
  
  changeEmail(newEmail: Email): void {
    if (this._email.equals(newEmail)) {
      return; // No change needed
    }
    
    const oldEmail = this._email.value;
    this._email = newEmail;
    this._updatedAt = new Date();
    
    this.addDomainEvent(new UserEmailChangedEvent({
      userId: this.id,
      oldEmail,
      newEmail: newEmail.value
    }));
  }
  
  deactivate(): void {
    if (this._status === UserStatus.INACTIVE) {
      throw new BusinessRuleViolationError('User is already inactive');
    }
    
    this._status = UserStatus.INACTIVE;
    this._updatedAt = new Date();
    
    this.addDomainEvent(new UserDeactivatedEvent({
      userId: this.id,
      email: this._email.value
    }));
  }
  
  // Getters
  get id(): UserId { return this._id; }
  get name(): UserName { return this._name; }
  get email(): Email { return this._email; }
  get status(): UserStatus { return this._status; }
  get createdAt(): Date { return this._createdAt; }
  get updatedAt(): Date { return this._updatedAt; }
  
  // Domain Events handling
  get domainEvents(): DomainEvent[] {
    return [...this._domainEvents];
  }
  
  clearDomainEvents(): void {
    this._domainEvents = [];
  }
  
  private addDomainEvent(event: DomainEvent): void {
    this._domainEvents.push(event);
  }
}

// domain/value-objects/user-id.value-object.ts
export class UserId {
  private constructor(private readonly _value: string) {}
  
  static generate(): UserId {
    return new UserId(crypto.randomUUID());
  }
  
  static fromString(value: string): UserId {
    if (!value || !this.isValidUUID(value)) {
      throw new ValidationException('Invalid UserId format');
    }
    return new UserId(value);
  }
  
  get value(): string {
    return this._value;
  }
  
  equals(other: UserId): boolean {
    return this._value === other._value;
  }
  
  private static isValidUUID(value: string): boolean {
    const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
    return uuidRegex.test(value);
  }
}
```

### 3. Aggregates e Aggregate Roots
```typescript
// domain/aggregates/order.aggregate.ts
export class Order {
  private static readonly MAX_ORDER_ITEMS = 50;
  
  private _domainEvents: DomainEvent[] = [];
  
  private constructor(
    private readonly _id: OrderId,
    private readonly _customerId: CustomerId,
    private _items: OrderItem[],
    private _status: OrderStatus,
    private _totalAmount: Money,
    private readonly _createdAt: Date,
    private _updatedAt: Date
  ) {}
  
  static create(props: {
    customerId: CustomerId;
    items: Array<{ productId: ProductId; quantity: number; unitPrice: Money }>;
  }): Order {
    if (props.items.length === 0) {
      throw new BusinessRuleViolationError('Order must have at least one item');
    }
    
    if (props.items.length > Order.MAX_ORDER_ITEMS) {
      throw new BusinessRuleViolationError(`Order cannot have more than ${Order.MAX_ORDER_ITEMS} items`);
    }
    
    const orderItems = props.items.map(item => 
      OrderItem.create({
        productId: item.productId,
        quantity: item.quantity,
        unitPrice: item.unitPrice
      })
    );
    
    const totalAmount = Order.calculateTotal(orderItems);
    
    const order = new Order(
      OrderId.generate(),
      props.customerId,
      orderItems,
      OrderStatus.PENDING,
      totalAmount,
      new Date(),
      new Date()
    );
    
    order.addDomainEvent(new OrderCreatedEvent({
      orderId: order.id,
      customerId: order.customerId,
      totalAmount: totalAmount.value,
      itemCount: orderItems.length
    }));
    
    return order;
  }
  
  addItem(productId: ProductId, quantity: number, unitPrice: Money): void {
    this.ensureCanAddItems();
    
    const existingItem = this._items.find(item => 
      item.productId.equals(productId)
    );
    
    if (existingItem) {
      existingItem.increaseQuantity(quantity);
    } else {
      if (this._items.length >= Order.MAX_ORDER_ITEMS) {
        throw new BusinessRuleViolationError(`Cannot exceed ${Order.MAX_ORDER_ITEMS} items per order`);
      }
      
      this._items.push(OrderItem.create({
        productId,
        quantity,
        unitPrice
      }));
    }
    
    this.recalculateTotal();
    this._updatedAt = new Date();
    
    this.addDomainEvent(new OrderItemAddedEvent({
      orderId: this.id,
      productId,
      quantity
    }));
  }
  
  confirm(): void {
    if (this._status !== OrderStatus.PENDING) {
      throw new BusinessRuleViolationError('Only pending orders can be confirmed');
    }
    
    this._status = OrderStatus.CONFIRMED;
    this._updatedAt = new Date();
    
    this.addDomainEvent(new OrderConfirmedEvent({
      orderId: this.id,
      customerId: this.customerId,
      totalAmount: this._totalAmount.value
    }));
  }
  
  private ensureCanAddItems(): void {
    if (this._status !== OrderStatus.PENDING) {
      throw new BusinessRuleViolationError('Cannot modify confirmed or cancelled orders');
    }
  }
  
  private recalculateTotal(): void {
    this._totalAmount = Order.calculateTotal(this._items);
  }
  
  private static calculateTotal(items: OrderItem[]): Money {
    const total = items.reduce((sum, item) => sum + item.totalPrice.value, 0);
    return Money.create(total);
  }
  
  // Getters
  get id(): OrderId { return this._id; }
  get customerId(): CustomerId { return this._customerId; }
  get items(): readonly OrderItem[] { return this._items; }
  get status(): OrderStatus { return this._status; }
  get totalAmount(): Money { return this._totalAmount; }
  get createdAt(): Date { return this._createdAt; }
  get updatedAt(): Date { return this._updatedAt; }
  
  // Domain events
  get domainEvents(): DomainEvent[] { return [...this._domainEvents]; }
  clearDomainEvents(): void { this._domainEvents = []; }
  private addDomainEvent(event: DomainEvent): void { this._domainEvents.push(event); }
}
```

### 4. Domain Services
```typescript
// domain/services/user-uniqueness.domain-service.ts
export class UserUniquenessService {
  constructor(private readonly userRepository: IUserRepository) {}
  
  async ensureEmailIsUnique(email: Email, excludeUserId?: UserId): Promise<void> {
    const existingUser = await this.userRepository.findByEmail(email);
    
    if (existingUser && (!excludeUserId || !existingUser.id.equals(excludeUserId))) {
      throw new BusinessRuleViolationError('Email is already in use');
    }
  }
}

// domain/services/order-pricing.domain-service.ts
export class OrderPricingService {
  constructor(
    private readonly pricingRules: IPricingRulesRepository,
    private readonly customerService: ICustomerService
  ) {}
  
  async calculateOrderTotal(
    customerId: CustomerId,
    items: Array<{ productId: ProductId; quantity: number }>
  ): Promise<Money> {
    const customer = await this.customerService.findById(customerId);
    if (!customer) {
      throw new NotFoundError('Customer not found');
    }
    
    const pricing = await this.pricingRules.findByCustomerType(customer.type);
    
    let total = Money.zero();
    
    for (const item of items) {
      const basePrice = await this.getProductPrice(item.productId);
      const discountedPrice = pricing.applyDiscount(basePrice, item.quantity);
      total = total.add(discountedPrice.multiply(item.quantity));
    }
    
    return total;
  }
  
  private async getProductPrice(productId: ProductId): Promise<Money> {
    // Implementation
  }
}
```

## Domain Events

### Base Domain Event
```typescript
// domain/events/base-domain.event.ts
export abstract class DomainEvent {
  public readonly occurredOn: Date;
  public readonly eventVersion: number;
  
  constructor(
    public readonly aggregateId: string,
    eventVersion: number = 1
  ) {
    this.occurredOn = new Date();
    this.eventVersion = eventVersion;
  }
  
  abstract get eventName(): string;
}

// domain/events/user-created.event.ts
export class UserCreatedEvent extends DomainEvent {
  constructor(
    public readonly data: {
      userId: UserId;
      email: string;
      name: string;
    }
  ) {
    super(data.userId.value);
  }
  
  get eventName(): string {
    return 'user.created';
  }
}
```

### Event Publisher
```typescript
// domain/services/domain-event-publisher.interface.ts
export interface IDomainEventPublisher {
  publish<T extends DomainEvent>(event: T): Promise<void>;
  publishMany<T extends DomainEvent>(events: T[]): Promise<void>;
}

// infrastructure/events/nestjs-domain-event-publisher.ts
@Injectable()
export class NestJSDomainEventPublisher implements IDomainEventPublisher {
  constructor(private readonly eventEmitter: EventEmitter2) {}
  
  async publish<T extends DomainEvent>(event: T): Promise<void> {
    await this.eventEmitter.emitAsync(event.eventName, event);
  }
  
  async publishMany<T extends DomainEvent>(events: T[]): Promise<void> {
    await Promise.all(events.map(event => this.publish(event)));
  }
}
```

## Repository Pattern

### Repository Interface
```typescript
// domain/repositories/user.repository.interface.ts
export interface IUserRepository {
  save(user: User): Promise<void>;
  findById(id: UserId): Promise<User | null>;
  findByEmail(email: Email): Promise<User | null>;
  findMany(specification: ISpecification<User>): Promise<User[]>;
  count(specification: ISpecification<User>): Promise<number>;
  delete(id: UserId): Promise<void>;
}

// domain/specifications/specification.interface.ts
export interface ISpecification<T> {
  isSatisfiedBy(candidate: T): boolean;
  and(other: ISpecification<T>): ISpecification<T>;
  or(other: ISpecification<T>): ISpecification<T>;
  not(): ISpecification<T>;
}
```

### Specifications
```typescript
// domain/specifications/user-specifications.ts
export class ActiveUserSpecification implements ISpecification<User> {
  isSatisfiedBy(user: User): boolean {
    return user.status === UserStatus.ACTIVE;
  }
  
  and(other: ISpecification<User>): ISpecification<User> {
    return new AndSpecification(this, other);
  }
  
  or(other: ISpecification<User>): ISpecification<User> {
    return new OrSpecification(this, other);
  }
  
  not(): ISpecification<User> {
    return new NotSpecification(this);
  }
}

export class EmailDomainSpecification implements ISpecification<User> {
  constructor(private readonly domain: string) {}
  
  isSatisfiedBy(user: User): boolean {
    return user.email.value.endsWith(`@${this.domain}`);
  }
  
  // ... outros métodos
}
```

## Anti-corruption Layer

```typescript
// infrastructure/anti-corruption/external-user.mapper.ts
export class ExternalUserMapper {
  static toDomain(externalUser: ExternalUserDto): User {
    try {
      const email = Email.create(externalUser.email_address);
      const name = UserName.create(
        externalUser.first_name,
        externalUser.last_name
      );
      
      return User.reconstitute({
        id: UserId.fromString(externalUser.external_id),
        name,
        email,
        status: this.mapStatus(externalUser.status),
        createdAt: new Date(externalUser.created_date),
        updatedAt: new Date(externalUser.modified_date)
      });
    } catch (error) {
      throw new IntegrationException(
        `Failed to map external user: ${error.message}`,
        { externalUser }
      );
    }
  }
  
  private static mapStatus(externalStatus: string): UserStatus {
    const statusMap: Record<string, UserStatus> = {
      'enabled': UserStatus.ACTIVE,
      'disabled': UserStatus.INACTIVE,
      'suspended': UserStatus.SUSPENDED,
    };
    
    const status = statusMap[externalStatus.toLowerCase()];
    if (!status) {
      throw new IntegrationException(`Unknown external status: ${externalStatus}`);
    }
    
    return status;
  }
}
```

## Ubiquitous Language

### Glossário de Termos
```typescript
// domain/shared/ubiquitous-language.ts
/**
 * Glossário do Domínio - User Management Context
 * 
 * Termos acordados com domain experts:
 * 
 * - User: Pessoa que tem acesso ao sistema
 * - Customer: User que pode fazer compras (extends User)
 * - Account: Conjunto de dados de autenticação do User
 * - Profile: Informações públicas do User
 * - Subscription: Relação do Customer com um plano de serviço
 * - Deactivation: User continua existindo mas não pode acessar
 * - Deletion: Remoção completa do User (GDPR compliance)
 */

export enum UserType {
  INDIVIDUAL = 'individual',    // Pessoa física
  BUSINESS = 'business',        // Pessoa jurídica  
  SYSTEM = 'system'             // Usuário de sistema/integração
}

export enum AccountStatus {
  PENDING_VERIFICATION = 'pending_verification',  // Aguardando confirmação
  ACTIVE = 'active',                             // Conta ativa
  SUSPENDED = 'suspended',                       // Suspensa temporariamente
  DEACTIVATED = 'deactivated',                  // Desativada pelo usuário
  BANNED = 'banned'                             // Banida por violação
}
```

## Validação e Business Rules

### Domain Rules Engine
```typescript
// domain/rules/business-rules.engine.ts
export class BusinessRulesEngine {
  private rules: Map<string, IBusinessRule[]> = new Map();
  
  registerRule(entityType: string, rule: IBusinessRule): void {
    if (!this.rules.has(entityType)) {
      this.rules.set(entityType, []);
    }
    this.rules.get(entityType)!.push(rule);
  }
  
  async validateRules(entityType: string, entity: any): Promise<void> {
    const entityRules = this.rules.get(entityType) || [];
    
    for (const rule of entityRules) {
      const isValid = await rule.isValid(entity);
      if (!isValid) {
        throw new BusinessRuleViolationError(rule.errorMessage);
      }
    }
  }
}

// domain/rules/user-business-rules.ts
export class MinimumAgeRule implements IBusinessRule {
  constructor(private readonly minimumAge: number = 18) {}
  
  async isValid(user: User): Promise<boolean> {
    if (!user.dateOfBirth) return true; // Optional field
    
    const age = this.calculateAge(user.dateOfBirth);
    return age >= this.minimumAge;
  }
  
  get errorMessage(): string {
    return `User must be at least ${this.minimumAge} years old`;
  }
  
  private calculateAge(dateOfBirth: Date): number {
    const today = new Date();
    let age = today.getFullYear() - dateOfBirth.getFullYear();
    const monthDiff = today.getMonth() - dateOfBirth.getMonth();
    
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < dateOfBirth.getDate())) {
      age--;
    }
    
    return age;
  }
}
```

## Checklist de Qualidade DDD

### ✅ Modelagem Estratégica
- [ ] Bounded contexts claramente definidos
- [ ] Context mapping documentado
- [ ] Ubiquitous language estabelecido
- [ ] Core domain identificado
- [ ] Anti-corruption layers implementadas

### ✅ Modelagem Tática  
- [ ] Entities com identity forte
- [ ] Value objects imutáveis
- [ ] Aggregates respeitam invariants
- [ ] Domain services para lógica não pertencente a entidades
- [ ] Repository pattern implementado
- [ ] Domain events para comunicação

### ✅ Qualidade do Código
- [ ] Business rules no domínio
- [ ] Validações no domínio
- [ ] Testes de domínio isolados
- [ ] Domain model rico em comportamento
- [ ] Separação clara de responsabilidades

## Métricas DDD

### KPIs para Enterprise
- **Domain Coverage**: >90% das regras de negócio no domain layer
- **Anemic Model Index**: <20% de classes apenas com getters/setters
- **Aggregate Consistency**: 100% das invariants testadas
- **Event Coverage**: >80% das operações importantes gerando eventos

## Referências Técnicas
- [Domain-Driven Design by Eric Evans](https://www.domainlanguage.com/ddd/)
- [Implementing Domain-Driven Design by Vaughn Vernon](https://vaughnvernon.co/)
- [.NET Microservices: Architecture for Containerized Applications](https://docs.microsoft.com/en-us/dotnet/architecture/microservices/)
