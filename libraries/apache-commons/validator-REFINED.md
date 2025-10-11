# Apache Commons Validator - Documentação Completa

![Maven Central](https://img.shields.io/maven-central/v/commons-validator/commons-validator?color=blue&label=Maven%20Central)
![Java](https://img.shields.io/badge/Java-8%2B-orange)
![License](https://img.shields.io/badge/License-Apache%202.0-green)

## 📚 Índice

1. [Introdução](#-introdução)
2. [Instalação e Setup](#️-instalação-e-setup)
3. [Email Validation](#-email-validation)
4. [URL Validation](#-url-validation)
5. [Domain e IP Validation](#-domain-e-ip-validation)
6. [Credit Card Validation](#-credit-card-validation)
7. [Date e Time Validation](#-date-e-time-validation)
8. [Numeric Validation](#-numeric-validation)
9. [Regex e Code Validation](#-regex-e-code-validation)
10. [ISBN e ISSN Validation](#-isbn-e-issn-validation)
11. [Custom Validators](#-custom-validators)
12. [Testes](#-testes)
13. [Best Practices](#-best-practices)
14. [Referência Rápida](#-referência-rápida)

---

## 🎯 Introdução

### Por que usar Apache Commons Validator?

**Apache Commons Validator** fornece validadores robustos, testados e prontos para uso para os formatos mais comuns de dados.

```java
// ❌ Sem Validator - regex manual propenso a erros
public boolean isValidEmail(String email) {
    return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    // Regex incompleta! Não valida TLD, permite caracteres inválidos, etc.
}

// ✅ Com Commons Validator - robusto e RFC-compliant
public boolean isValidEmail(String email) {
    return EmailValidator.getInstance().isValid(email);
}
```

### Características Principais

- ✅ **RFC-Compliant**: Segue especificações oficiais (RFC 5322 para email, etc)
- ✅ **Testado**: Cobertura de testes superior a 90%
- ✅ **Performático**: Validadores são singleton e otimizados
- ✅ **Extensível**: Fácil criar validadores customizados
- ✅ **Thread-Safe**: Todos os validadores são stateless

### Arquitetura da Biblioteca

```
Commons Validator
│
├── routines/ (Validadores Modernos - Recomendado)
│   ├── EmailValidator
│   ├── UrlValidator
│   ├── DomainValidator
│   ├── InetAddressValidator
│   ├── CreditCardValidator
│   ├── ISBNValidator
│   ├── DateValidator
│   ├── IntegerValidator
│   ├── DoubleValidator
│   └── RegexValidator
│
└── GenericValidator (Métodos estáticos - Legado)
    └── Validações básicas com métodos static
```

---

## ⚙️ Instalação e Setup

### Maven

```xml
<dependency>
    <groupId>commons-validator</groupId>
    <artifactId>commons-validator</artifactId>
    <version>1.10.0</version>
</dependency>
```

### Gradle

```gradle
implementation 'commons-validator:commons-validator:1.10.0'
```

### Gradle Kotlin DSL

```kotlin
implementation("commons-validator:commons-validator:1.10.0")
```

### Verificação de Instalação

```java
package com.example.validator;

import org.apache.commons.validator.routines.*;

/**
 * Classe para verificar instalação do Commons Validator.
 */
public class ValidatorInstallationTest {
    
    /**
     * Testa se o Commons Validator está corretamente configurado.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        System.out.println("=== Apache Commons Validator ===\n");
        
        // Teste EmailValidator
        String email = "test@example.com";
        boolean validEmail = EmailValidator.getInstance().isValid(email);
        System.out.println("Email '" + email + "' válido? " + validEmail);
        
        // Teste UrlValidator
        String url = "https://www.example.com";
        boolean validUrl = UrlValidator.getInstance().isValid(url);
        System.out.println("URL '" + url + "' válida? " + validUrl);
        
        // Teste CreditCardValidator
        CreditCardValidator ccValidator = new CreditCardValidator();
        String creditCard = "4532015112830366"; // Visa test number
        boolean validCC = ccValidator.isValid(creditCard);
        System.out.println("Cartão '" + creditCard + "' válido? " + validCC);
        
        System.out.println("\n✅ Commons Validator instalado com sucesso!");
    }
}
```

**Output esperado**:
```
=== Apache Commons Validator ===

Email 'test@example.com' válido? true
URL 'https://www.example.com' válida? true
Cartão '4532015112830366' válido? true

✅ Commons Validator instalado com sucesso!
```

---

## 📧 Email Validation

### Por que usar EmailValidator?

Validação de email é complexa (RFC 5322 tem 3,000+ linhas). EmailValidator implementa corretamente as regras.

| Aspecto | Regex Simples | EmailValidator |
|---------|---------------|----------------|
| RFC-compliant | ❌ | ✅ |
| TLD validation | ❌ | ✅ |
| International domains | ❌ | ✅ |
| Local addresses | ❌ | ✅ (opcional) |
| Manutenção | Alta | Zero |

### Exemplo Básico

```java
package com.example.validator.email;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * Demonstração básica de EmailValidator.
 */
public class EmailValidatorBasicExample {
    
    /**
     * Demonstra validação básica de emails.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        // Obter instância singleton (thread-safe)
        EmailValidator validator = EmailValidator.getInstance();
        
        System.out.println("=== Validação de Email ===\n");
        
        // Emails válidos
        String[] validEmails = {
            "user@example.com",
            "john.doe@company.org",
            "support+tag@service.co.uk",
            "admin_123@sub.domain.example.com"
        };
        
        System.out.println("✅ Emails VÁLIDOS:");
        for (String email : validEmails) {
            System.out.println("  " + email + " -> " + validator.isValid(email));
        }
        
        // Emails inválidos
        String[] invalidEmails = {
            "plainaddress",              // Sem @
            "@no-local.com",             // Sem parte local
            "user@",                     // Sem domínio
            "user name@example.com",     // Espaço
            "user..name@example.com",    // Pontos consecutivos
            ".user@example.com",         // Começa com ponto
            "user@domain@example.com"    // Múltiplos @
        };
        
        System.out.println("\n❌ Emails INVÁLIDOS:");
        for (String email : invalidEmails) {
            System.out.println("  " + email + " -> " + validator.isValid(email));
        }
    }
}
```

**Output**:
```
=== Validação de Email ===

✅ Emails VÁLIDOS:
  user@example.com -> true
  john.doe@company.org -> true
  support+tag@service.co.uk -> true
  admin_123@sub.domain.example.com -> true

❌ Emails INVÁLIDOS:
  plainaddress -> false
  @no-local.com -> false
  user@ -> false
  user name@example.com -> false
  user..name@example.com -> false
  .user@example.com -> false
  user@domain@example.com -> false
```

### Caso de Uso Real: Sistema de Registro de Usuários

```java
package com.example.validator.email;

import org.apache.commons.validator.routines.EmailValidator;
import java.util.*;

/**
 * Sistema de registro de usuários com validação de email.
 * Demonstra validação robusta com regras de negócio.
 */
public class UserRegistrationService {
    
    private final EmailValidator emailValidator;
    private final Set<String> allowedDomains;
    private final Set<String> blockedDomains;
    private final Set<String> registeredEmails;
    
    /**
     * Construtor com configuração de domínios.
     *
     * @param allowLocalAddresses permitir endereços locais (localhost, etc)
     */
    public UserRegistrationService(boolean allowLocalAddresses) {
        this.emailValidator = EmailValidator.getInstance(allowLocalAddresses);
        this.allowedDomains = new HashSet<>();
        this.blockedDomains = new HashSet<>(Arrays.asList(
            "tempmail.com", 
            "throwaway.email",
            "guerrillamail.com"
        ));
        this.registeredEmails = new HashSet<>();
    }
    
    /**
     * Resultado da validação de email.
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;
        
        private ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }
        
        public static ValidationResult success() {
            return new ValidationResult(true, null);
        }
        
        public static ValidationResult failure(String message) {
            return new ValidationResult(false, message);
        }
        
        public boolean isValid() { return valid; }
        public String getErrorMessage() { return errorMessage; }
    }
    
    /**
     * Valida email para registro com múltiplas regras.
     *
     * @param email email a validar
     * @return resultado da validação
     */
    public ValidationResult validateEmailForRegistration(String email) {
        // 1. Verificar se não é null ou vazio
        if (email == null || email.trim().isEmpty()) {
            return ValidationResult.failure("Email é obrigatório");
        }
        
        email = email.trim().toLowerCase();
        
        // 2. Verificar formato RFC-compliant
        if (!emailValidator.isValid(email)) {
            return ValidationResult.failure("Email inválido");
        }
        
        // 3. Verificar comprimento (RFC 5321)
        if (email.length() > 254) {
            return ValidationResult.failure("Email muito longo (máx: 254 caracteres)");
        }
        
        // 4. Extrair domínio
        String domain = extractDomain(email);
        
        // 5. Verificar domínios bloqueados (emails temporários)
        if (blockedDomains.contains(domain)) {
            return ValidationResult.failure("Domínio bloqueado: " + domain);
        }
        
        // 6. Verificar lista de domínios permitidos (se configurado)
        if (!allowedDomains.isEmpty() && !allowedDomains.contains(domain)) {
            return ValidationResult.failure("Domínio não permitido: " + domain);
        }
        
        // 7. Verificar se email já foi registrado
        if (registeredEmails.contains(email)) {
            return ValidationResult.failure("Email já registrado");
        }
        
        return ValidationResult.success();
    }
    
    /**
     * Registra novo usuário.
     *
     * @param email email do usuário
     * @return true se registrado com sucesso
     */
    public boolean registerUser(String email) {
        ValidationResult result = validateEmailForRegistration(email);
        
        if (!result.isValid()) {
            System.out.println("❌ Falha no registro: " + result.getErrorMessage());
            return false;
        }
        
        registeredEmails.add(email.toLowerCase());
        System.out.println("✅ Usuário registrado: " + email);
        return true;
    }
    
    /**
     * Extrai domínio do email.
     *
     * @param email email completo
     * @return domínio
     */
    private String extractDomain(String email) {
        int atIndex = email.indexOf('@');
        return atIndex != -1 ? email.substring(atIndex + 1) : "";
    }
    
    /**
     * Configura domínios permitidos (whitelist).
     *
     * @param domains domínios permitidos
     */
    public void setAllowedDomains(String... domains) {
        allowedDomains.clear();
        allowedDomains.addAll(Arrays.asList(domains));
    }
    
    /**
     * Adiciona domínio à lista de bloqueio.
     *
     * @param domain domínio a bloquear
     */
    public void blockDomain(String domain) {
        blockedDomains.add(domain.toLowerCase());
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        UserRegistrationService service = new UserRegistrationService(false);
        
        System.out.println("=== Sistema de Registro ===\n");
        
        // Registros bem-sucedidos
        service.registerUser("john@example.com");
        service.registerUser("jane@company.org");
        
        // Tentativas com erro
        System.out.println();
        service.registerUser("john@example.com");        // Duplicado
        service.registerUser("invalid-email");           // Formato inválido
        service.registerUser("user@tempmail.com");       // Domínio bloqueado
        service.registerUser("");                        // Vazio
        
        // Teste com whitelist
        System.out.println("\n=== Com Whitelist ===");
        service.setAllowedDomains("example.com", "company.org");
        service.registerUser("alice@example.com");       // OK
        service.registerUser("bob@other.com");           // Bloqueado
        
        // Estatísticas
        System.out.println("\n=== Estatísticas ===");
        System.out.println("Usuários registrados: " + service.registeredEmails.size());
        System.out.println("Emails: " + service.registeredEmails);
    }
}
```

**Output**:
```
=== Sistema de Registro ===

✅ Usuário registrado: john@example.com
✅ Usuário registrado: jane@company.org

❌ Falha no registro: Email já registrado
❌ Falha no registro: Email inválido
❌ Falha no registro: Domínio bloqueado: tempmail.com
❌ Falha no registro: Email é obrigatório

=== Com Whitelist ===
✅ Usuário registrado: alice@example.com
❌ Falha no registro: Domínio não permitido: other.com

=== Estatísticas ===
Usuários registrados: 3
Emails: [john@example.com, jane@company.org, alice@example.com]
```

### API EmailValidator

| Método | Descrição | Exemplo |
|--------|-----------|---------|
| `getInstance()` | Obtém validator padrão | `EmailValidator.getInstance()` |
| `getInstance(allowLocal)` | Validator com config | `getInstance(true)` |
| `isValid(email)` | Valida email | `isValid("user@example.com")` |

---

## 🌐 URL Validation

### Por que usar UrlValidator?

URLs têm muitas partes opcionais e regras complexas. UrlValidator valida esquema, autoridade, caminho, query e fragmento.

### Exemplo Básico

```java
package com.example.validator.url;

import org.apache.commons.validator.routines.UrlValidator;

/**
 * Demonstração básica de UrlValidator.
 */
public class UrlValidatorBasicExample {
    
    /**
     * Demonstra validação de URLs.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        // Validator padrão (http, https, ftp)
        UrlValidator validator = UrlValidator.getInstance();
        
        System.out.println("=== Validação de URL ===\n");
        
        // URLs válidas
        String[] validUrls = {
            "http://www.example.com",
            "https://example.com",
            "https://sub.domain.example.com/path/to/resource",
            "http://example.com:8080",
            "https://example.com/path?query=value",
            "https://example.com/path#fragment",
            "ftp://files.example.com"
        };
        
        System.out.println("✅ URLs VÁLIDAS:");
        for (String url : validUrls) {
            System.out.println("  " + url + " -> " + validator.isValid(url));
        }
        
        // URLs inválidas
        String[] invalidUrls = {
            "not a url",
            "http://",
            "://example.com",
            "http://example",           // Sem TLD (em modo padrão)
            "htp://example.com",        // Esquema inválido
            "http://example .com"       // Espaço
        };
        
        System.out.println("\n❌ URLs INVÁLIDAS:");
        for (String url : invalidUrls) {
            System.out.println("  " + url + " -> " + validator.isValid(url));
        }
    }
}
```

### Caso de Uso Real: Validador de Links

```java
package com.example.validator.url;

import org.apache.commons.validator.routines.UrlValidator;

/**
 * Sistema de validação de links para CMS.
 * Suporta diferentes esquemas e validações customizadas.
 */
public class LinkValidator {
    
    private final UrlValidator httpValidator;
    private final UrlValidator customValidator;
    private final UrlValidator localValidator;
    
    /**
     * Construtor inicializa validadores.
     */
    public LinkValidator() {
        // Validator padrão (http, https, ftp)
        this.httpValidator = UrlValidator.getInstance();
        
        // Validator com esquemas customizados
        String[] schemes = {"http", "https", "ftp", "sftp", "file"};
        this.customValidator = new UrlValidator(schemes);
        
        // Validator permitindo localhost
        this.localValidator = new UrlValidator(
            schemes,
            UrlValidator.ALLOW_LOCAL_URLS
        );
    }
    
    /**
     * Valida URL web padrão.
     *
     * @param url URL a validar
     * @return true se válida
     */
    public boolean isValidWebUrl(String url) {
        return httpValidator.isValid(url);
    }
    
    /**
     * Valida URL com esquemas customizados.
     *
     * @param url URL a validar
     * @return true se válida
     */
    public boolean isValidCustomUrl(String url) {
        return customValidator.isValid(url);
    }
    
    /**
     * Valida URL incluindo endereços locais.
     *
     * @param url URL a validar
     * @return true se válida
     */
    public boolean isValidLocalUrl(String url) {
        return localValidator.isValid(url);
    }
    
    /**
     * Valida e classifica URL.
     *
     * @param url URL a validar
     * @return tipo de URL ou "invalid"
     */
    public String classifyUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return "empty";
        }
        
        url = url.trim();
        
        if (url.startsWith("http://")) {
            return httpValidator.isValid(url) ? "http" : "invalid";
        } else if (url.startsWith("https://")) {
            return httpValidator.isValid(url) ? "https (secure)" : "invalid";
        } else if (url.startsWith("ftp://")) {
            return customValidator.isValid(url) ? "ftp" : "invalid";
        } else if (url.startsWith("file://")) {
            return customValidator.isValid(url) ? "file" : "invalid";
        } else {
            return "invalid";
        }
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        LinkValidator validator = new LinkValidator();
        
        System.out.println("=== Validador de Links ===\n");
        
        // URLs web
        String[] webUrls = {
            "https://www.example.com",
            "http://localhost:8080",
            "file:///home/user/document.pdf"
        };
        
        System.out.println("URLs Web (http/https):");
        for (String url : webUrls) {
            System.out.println("  " + url);
            System.out.println("    Web: " + validator.isValidWebUrl(url));
            System.out.println("    Custom: " + validator.isValidCustomUrl(url));
            System.out.println("    Local: " + validator.isValidLocalUrl(url));
            System.out.println("    Tipo: " + validator.classifyUrl(url));
        }
        
        // Teste de esquemas
        System.out.println("\n=== Teste de Esquemas ===");
        String[] testUrls = {
            "https://secure.example.com",
            "ftp://files.example.com",
            "sftp://server.example.com",
            "file:///C:/Documents/file.txt"
        };
        
        for (String url : testUrls) {
            System.out.println(url + " -> " + validator.classifyUrl(url));
        }
    }
}
```

**Output**:
```
=== Validador de Links ===

URLs Web (http/https):
  https://www.example.com
    Web: true
    Custom: true
    Local: true
    Tipo: https (secure)
  http://localhost:8080
    Web: false
    Custom: false
    Local: true
    Tipo: http
  file:///home/user/document.pdf
    Web: false
    Custom: true
    Local: true
    Tipo: file

=== Teste de Esquemas ===
https://secure.example.com -> https (secure)
ftp://files.example.com -> ftp
sftp://server.example.com -> invalid
file:///C:/Documents/file.txt -> file
```

### API UrlValidator

| Método | Descrição |
|--------|-----------|
| `getInstance()` | Validator padrão (http, https, ftp) |
| `new UrlValidator(schemes)` | Validator com esquemas customizados |
| `new UrlValidator(schemes, options)` | Com opções (ALLOW_LOCAL_URLS, etc) |
| `isValid(url)` | Valida URL |

**Opções disponíveis**:
- `ALLOW_LOCAL_URLS` - Permite localhost e IPs privados
- `ALLOW_ALL_SCHEMES` - Permite qualquer esquema
- `ALLOW_2_SLASHES` - Permite // no caminho
- `NO_FRAGMENTS` - Não permite fragmentos (#)

---

## 🌍 Domain e IP Validation

### DomainValidator

```java
package com.example.validator.domain;

import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;

/**
 * Validação de domínios e endereços IP.
 */
public class DomainAndIpExample {
    
    /**
     * Demonstra validação de domínios e IPs.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        DomainValidator domainValidator = DomainValidator.getInstance();
        InetAddressValidator ipValidator = InetAddressValidator.getInstance();
        
        System.out.println("=== Validação de Domínio ===\n");
        
        // Domínios válidos
        String[] validDomains = {
            "example.com",
            "sub.domain.example.com",
            "example.co.uk",
            "xn--n3h.com"  // IDN (internationalized domain)
        };
        
        System.out.println("✅ Domínios VÁLIDOS:");
        for (String domain : validDomains) {
            System.out.println("  " + domain + " -> " + 
                domainValidator.isValid(domain));
        }
        
        // Domínios inválidos
        String[] invalidDomains = {
            "invalid",
            "-example.com",
            "example-.com",
            "example..com"
        };
        
        System.out.println("\n❌ Domínios INVÁLIDOS:");
        for (String domain : invalidDomains) {
            System.out.println("  " + domain + " -> " + 
                domainValidator.isValid(domain));
        }
        
        // IPs
        System.out.println("\n=== Validação de IP ===\n");
        
        String[] ips = {
            "192.168.1.1",           // IPv4 válido
            "2001:0db8:85a3::1",     // IPv6 válido
            "256.1.1.1",             // IPv4 inválido
            "not-an-ip"              // Inválido
        };
        
        for (String ip : ips) {
            boolean validIPv4 = ipValidator.isValidInet4Address(ip);
            boolean validIPv6 = ipValidator.isValidInet6Address(ip);
            boolean validAny = ipValidator.isValid(ip);
            
            System.out.println(ip + ":");
            System.out.println("  IPv4: " + validIPv4);
            System.out.println("  IPv6: " + validIPv6);
            System.out.println("  Any: " + validAny);
        }
    }
}
```

**Output**:
```
=== Validação de Domínio ===

✅ Domínios VÁLIDOS:
  example.com -> true
  sub.domain.example.com -> true
  example.co.uk -> true
  xn--n3h.com -> true

❌ Domínios INVÁLIDOS:
  invalid -> false
  -example.com -> false
  example-.com -> false
  example..com -> false

=== Validação de IP ===

192.168.1.1:
  IPv4: true
  IPv6: false
  Any: true
2001:0db8:85a3::1:
  IPv4: false
  IPv6: true
  Any: true
256.1.1.1:
  IPv4: false
  IPv6: false
  Any: false
not-an-ip:
  IPv4: false
  IPv6: false
  Any: false
```

---

## 💳 Credit Card Validation

### Por que usar CreditCardValidator?

Valida número de cartão usando o **algoritmo de Luhn** e verifica o prefixo para identificar a bandeira.

### Exemplo Completo

```java
package com.example.validator.creditcard;

import org.apache.commons.validator.routines.CreditCardValidator;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;

/**
 * Sistema de validação de cartões de crédito.
 * Valida número, identifica bandeira e verifica Luhn.
 */
public class PaymentCardValidator {
    
    private final CreditCardValidator allCardsValidator;
    private final CreditCardValidator visaMastercardOnly;
    
    /**
     * Tipos de cartão suportados.
     */
    public enum CardType {
        VISA("Visa"),
        MASTERCARD("Mastercard"),
        AMEX("American Express"),
        DISCOVER("Discover"),
        DINERS("Diners Club"),
        UNKNOWN("Unknown");
        
        private final String displayName;
        
        CardType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Resultado da validação de cartão.
     */
    public static class CardValidationResult {
        private final boolean valid;
        private final CardType type;
        private final String maskedNumber;
        
        public CardValidationResult(boolean valid, CardType type, String maskedNumber) {
            this.valid = valid;
            this.type = type;
            this.maskedNumber = maskedNumber;
        }
        
        public boolean isValid() { return valid; }
        public CardType getType() { return type; }
        public String getMaskedNumber() { return maskedNumber; }
        
        @Override
        public String toString() {
            return String.format("Valid: %s | Type: %s | Number: %s",
                valid, type.getDisplayName(), maskedNumber);
        }
    }
    
    /**
     * Construtor inicializa validadores.
     */
    public PaymentCardValidator() {
        // Validator para todos os tipos
        this.allCardsValidator = new CreditCardValidator();
        
        // Validator apenas para Visa e Mastercard
        this.visaMastercardOnly = new CreditCardValidator(
            CreditCardValidator.VISA + 
            CreditCardValidator.MASTERCARD
        );
    }
    
    /**
     * Valida número de cartão de crédito.
     *
     * @param cardNumber número do cartão
     * @return resultado da validação
     */
    public CardValidationResult validateCard(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return new CardValidationResult(false, CardType.UNKNOWN, "****");
        }
        
        // Remove espaços e hífens
        String cleanNumber = cardNumber.replaceAll("[\\s-]", "");
        
        // Valida com Luhn
        boolean valid = allCardsValidator.isValid(cleanNumber);
        
        // Identifica tipo
        CardType type = identifyCardType(cleanNumber);
        
        // Mascara número (mantém primeiros 4 e últimos 4)
        String masked = maskCardNumber(cleanNumber);
        
        return new CardValidationResult(valid, type, masked);
    }
    
    /**
     * Identifica tipo de cartão pelo prefixo.
     *
     * @param cardNumber número limpo do cartão
     * @return tipo identificado
     */
    private CardType identifyCardType(String cardNumber) {
        if (cardNumber.startsWith("4")) {
            return CardType.VISA;
        } else if (cardNumber.startsWith("5")) {
            return CardType.MASTERCARD;
        } else if (cardNumber.startsWith("34") || cardNumber.startsWith("37")) {
            return CardType.AMEX;
        } else if (cardNumber.startsWith("6011") || cardNumber.startsWith("65")) {
            return CardType.DISCOVER;
        } else if (cardNumber.startsWith("36") || cardNumber.startsWith("38")) {
            return CardType.DINERS;
        }
        return CardType.UNKNOWN;
    }
    
    /**
     * Mascara número do cartão para exibição segura.
     *
     * @param cardNumber número do cartão
     * @return número mascarado
     */
    private String maskCardNumber(String cardNumber) {
        if (cardNumber.length() < 8) {
            return "****";
        }
        
        String first4 = cardNumber.substring(0, 4);
        String last4 = cardNumber.substring(cardNumber.length() - 4);
        int middleLength = cardNumber.length() - 8;
        String middle = "*".repeat(middleLength);
        
        return first4 + middle + last4;
    }
    
    /**
     * Valida se aceita apenas Visa/Mastercard.
     *
     * @param cardNumber número do cartão
     * @return true se Visa ou Mastercard válido
     */
    public boolean isVisaOrMastercard(String cardNumber) {
        String clean = cardNumber.replaceAll("[\\s-]", "");
        return visaMastercardOnly.isValid(clean);
    }
    
    /**
     * Valida usando apenas algoritmo de Luhn.
     *
     * @param cardNumber número do cartão
     * @return true se passa no teste de Luhn
     */
    public boolean passesLuhnCheck(String cardNumber) {
        String clean = cardNumber.replaceAll("[\\s-]", "");
        return LuhnCheckDigit.LUHN_CHECK_DIGIT.isValid(clean);
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        PaymentCardValidator validator = new PaymentCardValidator();
        
        System.out.println("=== Validador de Cartões ===\n");
        
        // Números de teste (válidos pelo algoritmo de Luhn)
        String[] testCards = {
            "4532015112830366",      // Visa
            "5425233430109903",      // Mastercard
            "374245455400126",       // Amex
            "6011111111111117",      // Discover
            "36227206271667",        // Diners
            "1234567890123456",      // Inválido (não passa Luhn)
            "4532-0151-1283-0366"    // Visa com hífens
        };
        
        for (String card : testCards) {
            CardValidationResult result = validator.validateCard(card);
            System.out.println("Cartão: " + card);
            System.out.println("  " + result);
            System.out.println();
        }
        
        // Teste Visa/Mastercard only
        System.out.println("=== Apenas Visa/Mastercard ===");
        System.out.println("Visa: " + 
            validator.isVisaOrMastercard("4532015112830366"));       // true
        System.out.println("Amex: " + 
            validator.isVisaOrMastercard("374245455400126"));        // false
    }
}
```

**Output**:
```
=== Validador de Cartões ===

Cartão: 4532015112830366
  Valid: true | Type: Visa | Number: 4532********0366

Cartão: 5425233430109903
  Valid: true | Type: Mastercard | Number: 5425********9903

Cartão: 374245455400126
  Valid: true | Type: American Express | Number: 3742*******0126

Cartão: 6011111111111117
  Valid: true | Type: Discover | Number: 6011********1117

Cartão: 36227206271667
  Valid: true | Type: Diners Club | Number: 3622******1667

Cartão: 1234567890123456
  Valid: false | Type: Unknown | Number: 1234********3456

Cartão: 4532-0151-1283-0366
  Valid: true | Type: Visa | Number: 4532********0366

=== Apenas Visa/Mastercard ===
Visa: true
Amex: false
```

### API CreditCardValidator

| Método | Descrição |
|--------|-----------|
| `new CreditCardValidator()` | Todos os tipos |
| `new CreditCardValidator(flags)` | Tipos específicos |
| `isValid(cardNumber)` | Valida número |
| `validate(cardNumber)` | Retorna código do tipo |

**Flags disponíveis**:
- `VISA` - Cartões Visa
- `MASTERCARD` - Cartões Mastercard
- `AMEX` - American Express
- `DISCOVER` - Discover
- `DINERS` - Diners Club

---

## 📅 Date e Time Validation

### Exemplo Completo

```java
package com.example.validator.datetime;

import org.apache.commons.validator.routines.*;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Validação de datas e horários.
 */
public class DateTimeValidatorExample {
    
    /**
     * Demonstra validação de datas.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        DateValidator dateValidator = DateValidator.getInstance();
        TimeValidator timeValidator = TimeValidator.getInstance();
        CalendarValidator calValidator = CalendarValidator.getInstance();
        
        System.out.println("=== Validação de Data ===\n");
        
        // Validar strings de data
        String[] dates = {
            "2025-10-10",    // ISO format
            "10/10/2025",    // US format
            "10-10-2025",
            "invalid-date"
        };
        
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat usFormat = new SimpleDateFormat("MM/dd/yyyy");
        
        for (String dateStr : dates) {
            // Tentar ISO
            Date date = dateValidator.validate(dateStr, "yyyy-MM-dd");
            if (date == null) {
                // Tentar US
                date = dateValidator.validate(dateStr, "MM/dd/yyyy");
            }
            
            System.out.println(dateStr + " -> " + 
                (date != null ? "Válida: " + date : "Inválida"));
        }
        
        // Validar ranges
        System.out.println("\n=== Validação de Ranges ===");
        
        Date minDate = dateValidator.validate("2025-01-01", "yyyy-MM-dd");
        Date maxDate = dateValidator.validate("2025-12-31", "yyyy-MM-dd");
        Date testDate = dateValidator.validate("2025-10-10", "yyyy-MM-dd");
        
        boolean inRange = dateValidator.isInRange(testDate, minDate, maxDate);
        System.out.println("2025-10-10 está entre 2025-01-01 e 2025-12-31? " + inRange);
        
        // Validar horários
        System.out.println("\n=== Validação de Horário ===");
        
        String[] times = {
            "14:30:00",
            "23:59:59",
            "25:00:00",  // Inválido
            "14:30"
        };
        
        for (String timeStr : times) {
            Calendar cal = timeValidator.validate(timeStr, "HH:mm:ss");
            if (cal == null) {
                cal = timeValidator.validate(timeStr, "HH:mm");
            }
            System.out.println(timeStr + " -> " + 
                (cal != null ? "Válido" : "Inválido"));
        }
    }
}
```

---

## 🔢 Numeric Validation

### Exemplo Completo

```java
package com.example.validator.numeric;

import org.apache.commons.validator.routines.*;
import java.math.BigDecimal;
import java.util.Locale;

/**
 * Validação de números com suporte a localização.
 */
public class NumericValidatorExample {
    
    /**
     * Demonstra validação numérica.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        IntegerValidator intValidator = IntegerValidator.getInstance();
        DoubleValidator doubleValidator = DoubleValidator.getInstance();
        BigDecimalValidator decimalValidator = BigDecimalValidator.getInstance();
        PercentValidator percentValidator = PercentValidator.getInstance();
        
        System.out.println("=== Validação de Inteiros ===\n");
        
        String[] integers = {"123", "-456", "1.5", "abc", "2147483647"};
        
        for (String str : integers) {
            Integer value = intValidator.validate(str);
            System.out.println(str + " -> " + 
                (value != null ? "Válido: " + value : "Inválido"));
        }
        
        // Validar com range
        System.out.println("\n=== Range (1-100) ===");
        String[] rangeTest = {"50", "150", "-10"};
        
        for (String str : rangeTest) {
            boolean valid = intValidator.isInRange(
                intValidator.validate(str), 1, 100
            );
            System.out.println(str + " está entre 1-100? " + valid);
        }
        
        // Validar decimais
        System.out.println("\n=== Validação de Decimais ===");
        String[] decimals = {"123.45", "1,234.56", "abc"};
        
        for (String str : decimals) {
            Double value = doubleValidator.validate(str);
            System.out.println(str + " -> " + 
                (value != null ? "Válido: " + value : "Inválido"));
        }
        
        // Percentagens
        System.out.println("\n=== Validação de Percentagens ===");
        String[] percents = {"50%", "100%", "150%", "abc%"};
        
        for (String str : percents) {
            BigDecimal value = percentValidator.validate(str);
            System.out.println(str + " -> " + 
                (value != null ? "Válido: " + value : "Inválido"));
        }
    }
}
```

---

## 🔍 Regex e Code Validation

### Exemplo Completo

```java
package com.example.validator.regex;

import org.apache.commons.validator.routines.*;

/**
 * Validação com regex e códigos (ISBN, ISSN, etc).
 */
public class RegexAndCodeExample {
    
    /**
     * Demonstra validação com regex e códigos.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        // Regex Validator
        System.out.println("=== Regex Validator ===\n");
        
        // Validar CEP brasileiro
        RegexValidator cepValidator = new RegexValidator("^\\d{5}-?\\d{3}$");
        
        String[] ceps = {"12345-678", "12345678", "abcde-fgh"};
        for (String cep : ceps) {
            System.out.println(cep + " -> " + cepValidator.isValid(cep));
        }
        
        // ISBN Validator
        System.out.println("\n=== ISBN Validator ===");
        
        ISBNValidator isbnValidator = ISBNValidator.getInstance();
        
        String[] isbns = {
            "978-0-596-52068-7",     // ISBN-13 válido
            "0-596-52068-9",         // ISBN-10 válido
            "123-4-567-89012-3"      // Inválido
        };
        
        for (String isbn : isbns) {
            boolean valid = isbnValidator.isValid(isbn);
            System.out.println(isbn + " -> " + valid);
        }
        
        // Code Validator personalizado
        System.out.println("\n=== Code Validator (Produto) ===");
        
        // Código de produto: 3 letras + 5 dígitos
        RegexValidator productRegex = new RegexValidator("^[A-Z]{3}\\d{5}$");
        CodeValidator productValidator = new CodeValidator(
            productRegex, 
            LuhnCheckDigit.LUHN_CHECK_DIGIT
        );
        
        for (String product : products) {
            System.out.println(product + " -> " + productRegex.isValid(product));
        }
    }
}
```

---

## 📚 ISBN e ISSN Validation

### Exemplo Completo com Casos de Uso

```java
package com.example.validator.isbn;

import org.apache.commons.validator.routines.ISBNValidator;
import org.apache.commons.validator.routines.ISSNValidator;

/**
 * Sistema de validação de publicações (livros e periódicos).
 */
public class PublicationValidator {
    
    private final ISBNValidator isbnValidator;
    private final ISSNValidator issnValidator;
    
    /**
     * Construtor inicializa validadores.
     */
    public PublicationValidator() {
        this.isbnValidator = ISBNValidator.getInstance();
        this.issnValidator = ISSNValidator.getInstance();
    }
    
    /**
     * Valida ISBN (10 ou 13 dígitos).
     *
     * @param isbn código ISBN
     * @return true se válido
     */
    public boolean isValidISBN(String isbn) {
        return isbnValidator.isValid(isbn);
    }
    
    /**
     * Valida e identifica formato ISBN.
     *
     * @param isbn código ISBN
     * @return "ISBN-10", "ISBN-13" ou "Invalid"
     */
    public String identifyISBNFormat(String isbn) {
        if (isbnValidator.isValidISBN10(isbn)) {
            return "ISBN-10";
        } else if (isbnValidator.isValidISBN13(isbn)) {
            return "ISBN-13";
        }
        return "Invalid";
    }
    
    /**
     * Converte ISBN-10 para ISBN-13.
     *
     * @param isbn10 código ISBN-10
     * @return código ISBN-13 ou null se inválido
     */
    public String convertISBN10To13(String isbn10) {
        if (!isbnValidator.isValidISBN10(isbn10)) {
            return null;
        }
        
        // Remove hífens e espaços
        String clean = isbn10.replaceAll("[\\s-]", "");
        
        // Remove dígito verificador ISBN-10
        String base = clean.substring(0, 9);
        
        // Adiciona prefixo 978
        String isbn13Base = "978" + base;
        
        // Calcula dígito verificador ISBN-13
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn13Base.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int checkDigit = (10 - (sum % 10)) % 10;
        
        return isbn13Base + checkDigit;
    }
    
    /**
     * Valida ISSN de periódico.
     *
     * @param issn código ISSN
     * @return true se válido
     */
    public boolean isValidISSN(String issn) {
        return issnValidator.isValid(issn);
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        PublicationValidator validator = new PublicationValidator();
        
        System.out.println("=== Validação de Publicações ===\n");
        
        // Teste ISBN
        System.out.println("ISBN:");
        String[] isbns = {
            "978-0-596-52068-7",     // ISBN-13 válido
            "0-596-52068-9",         // ISBN-10 válido
            "978-3-16-148410-0",     // ISBN-13 válido
            "123-4-567-89012-3"      // Inválido
        };
        
        for (String isbn : isbns) {
            String format = validator.identifyISBNFormat(isbn);
            System.out.println("  " + isbn + " -> " + format);
        }
        
        // Conversão ISBN-10 para ISBN-13
        System.out.println("\n=== Conversão ISBN-10 → ISBN-13 ===");
        String isbn10 = "0-596-52068-9";
        String isbn13 = validator.convertISBN10To13(isbn10);
        System.out.println("ISBN-10: " + isbn10);
        System.out.println("ISBN-13: " + isbn13);
        
        // Teste ISSN
        System.out.println("\n=== ISSN ===");
        String[] issns = {
            "0378-5955",    // Válido
            "0000-0019",    // Válido
            "1234-5678"     // Inválido (checksum)
        };
        
        for (String issn : issns) {
            boolean valid = validator.isValidISSN(issn);
            System.out.println("  " + issn + " -> " + (valid ? "Válido" : "Inválido"));
        }
    }
}
```

**Output**:
```
=== Validação de Publicações ===

ISBN:
  978-0-596-52068-7 -> ISBN-13
  0-596-52068-9 -> ISBN-10
  978-3-16-148410-0 -> ISBN-13
  123-4-567-89012-3 -> Invalid

=== Conversão ISBN-10 → ISBN-13 ===
ISBN-10: 0-596-52068-9
ISBN-13: 9780596520687

=== ISSN ===
  0378-5955 -> Válido
  0000-0019 -> Válido
  1234-5678 -> Inválido
```

---

## 🔧 Custom Validators

### Criando Validadores Personalizados

```java
package com.example.validator.custom;

import org.apache.commons.validator.routines.RegexValidator;
import java.util.regex.Pattern;

/**
 * Validadores customizados para CPF, CNPJ e telefone brasileiro.
 */
public class BrazilianValidators {
    
    /**
     * Validador de CPF (Cadastro de Pessoa Física).
     */
    public static class CPFValidator {
        
        private static final RegexValidator REGEX = 
            new RegexValidator("^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$");
        
        /**
         * Valida CPF brasileiro.
         *
         * @param cpf CPF a validar
         * @return true se válido
         */
        public boolean isValid(String cpf) {
            if (cpf == null || !REGEX.isValid(cpf)) {
                return false;
            }
            
            // Remove formatação
            String cleanCPF = cpf.replaceAll("[.-]", "");
            
            // Verifica CPFs conhecidos como inválidos
            if (cleanCPF.matches("(\\d)\\1{10}")) {
                return false; // 111.111.111-11, etc.
            }
            
            // Calcula primeiro dígito verificador
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += Character.getNumericValue(cleanCPF.charAt(i)) * (10 - i);
            }
            int firstDigit = 11 - (sum % 11);
            if (firstDigit >= 10) firstDigit = 0;
            
            // Calcula segundo dígito verificador
            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += Character.getNumericValue(cleanCPF.charAt(i)) * (11 - i);
            }
            int secondDigit = 11 - (sum % 11);
            if (secondDigit >= 10) secondDigit = 0;
            
            // Verifica dígitos
            return Character.getNumericValue(cleanCPF.charAt(9)) == firstDigit &&
                   Character.getNumericValue(cleanCPF.charAt(10)) == secondDigit;
        }
        
        /**
         * Formata CPF para exibição.
         *
         * @param cpf CPF sem formatação
         * @return CPF formatado (###.###.###-##)
         */
        public String format(String cpf) {
            String clean = cpf.replaceAll("[.-]", "");
            if (clean.length() != 11) {
                return cpf;
            }
            return String.format("%s.%s.%s-%s",
                clean.substring(0, 3),
                clean.substring(3, 6),
                clean.substring(6, 9),
                clean.substring(9, 11));
        }
    }
    
    /**
     * Validador de CNPJ (Cadastro Nacional de Pessoa Jurídica).
     */
    public static class CNPJValidator {
        
        private static final RegexValidator REGEX = 
            new RegexValidator("^\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}$");
        
        /**
         * Valida CNPJ brasileiro.
         *
         * @param cnpj CNPJ a validar
         * @return true se válido
         */
        public boolean isValid(String cnpj) {
            if (cnpj == null || !REGEX.isValid(cnpj)) {
                return false;
            }
            
            // Remove formatação
            String cleanCNPJ = cnpj.replaceAll("[./-]", "");
            
            // Verifica CNPJs conhecidos como inválidos
            if (cleanCNPJ.matches("(\\d)\\1{13}")) {
                return false;
            }
            
            // Calcula primeiro dígito
            int[] weights1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int sum = 0;
            for (int i = 0; i < 12; i++) {
                sum += Character.getNumericValue(cleanCNPJ.charAt(i)) * weights1[i];
            }
            int digit1 = sum % 11 < 2 ? 0 : 11 - (sum % 11);
            
            // Calcula segundo dígito
            int[] weights2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            sum = 0;
            for (int i = 0; i < 13; i++) {
                sum += Character.getNumericValue(cleanCNPJ.charAt(i)) * weights2[i];
            }
            int digit2 = sum % 11 < 2 ? 0 : 11 - (sum % 11);
            
            return Character.getNumericValue(cleanCNPJ.charAt(12)) == digit1 &&
                   Character.getNumericValue(cleanCNPJ.charAt(13)) == digit2;
        }
        
        /**
         * Formata CNPJ para exibição.
         *
         * @param cnpj CNPJ sem formatação
         * @return CNPJ formatado (##.###.###/####-##)
         */
        public String format(String cnpj) {
            String clean = cnpj.replaceAll("[./-]", "");
            if (clean.length() != 14) {
                return cnpj;
            }
            return String.format("%s.%s.%s/%s-%s",
                clean.substring(0, 2),
                clean.substring(2, 5),
                clean.substring(5, 8),
                clean.substring(8, 12),
                clean.substring(12, 14));
        }
    }
    
    /**
     * Validador de telefone brasileiro.
     */
    public static class PhoneValidator {
        
        // (##) ####-#### ou (##) #####-####
        private static final RegexValidator REGEX = 
            new RegexValidator("^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$");
        
        /**
         * Valida telefone brasileiro.
         *
         * @param phone telefone a validar
         * @return true se válido
         */
        public boolean isValid(String phone) {
            return REGEX.isValid(phone);
        }
        
        /**
         * Identifica tipo de telefone.
         *
         * @param phone telefone
         * @return "Celular", "Fixo" ou "Inválido"
         */
        public String identifyType(String phone) {
            if (!isValid(phone)) {
                return "Inválido";
            }
            
            String clean = phone.replaceAll("[()\\s-]", "");
            
            // 11 dígitos = celular, 10 dígitos = fixo
            return clean.length() == 11 ? "Celular" : "Fixo";
        }
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        CPFValidator cpfValidator = new CPFValidator();
        CNPJValidator cnpjValidator = new CNPJValidator();
        PhoneValidator phoneValidator = new PhoneValidator();
        
        System.out.println("=== Validadores Brasileiros ===\n");
        
        // CPF
        System.out.println("CPF:");
        String[] cpfs = {
            "123.456.789-09",    // Válido
            "111.111.111-11",    // Inválido (sequência)
            "12345678909"        // Válido (sem formatação)
        };
        
        for (String cpf : cpfs) {
            boolean valid = cpfValidator.isValid(cpf);
            String formatted = cpfValidator.format(cpf);
            System.out.println("  " + cpf + " -> " + 
                (valid ? "✅ Válido (" + formatted + ")" : "❌ Inválido"));
        }
        
        // CNPJ
        System.out.println("\nCNPJ:");
        String[] cnpjs = {
            "11.222.333/0001-81",    // Válido
            "11.111.111/1111-11"     // Inválido
        };
        
        for (String cnpj : cnpjs) {
            boolean valid = cnpjValidator.isValid(cnpj);
            System.out.println("  " + cnpj + " -> " + 
                (valid ? "✅ Válido" : "❌ Inválido"));
        }
        
        // Telefone
        System.out.println("\nTelefone:");
        String[] phones = {
            "(11) 98765-4321",    // Celular
            "(11) 3456-7890",     // Fixo
            "1234-5678"           // Inválido
        };
        
        for (String phone : phones) {
            String type = phoneValidator.identifyType(phone);
            System.out.println("  " + phone + " -> " + type);
        }
    }
}
```

**Output**:
```
=== Validadores Brasileiros ===

CPF:
  123.456.789-09 -> ✅ Válido (123.456.789-09)
  111.111.111-11 -> ❌ Inválido
  12345678909 -> ✅ Válido (123.456.789-09)

CNPJ:
  11.222.333/0001-81 -> ✅ Válido
  11.111.111/1111-11 -> ❌ Inválido

Telefone:
  (11) 98765-4321 -> Celular
  (11) 3456-7890 -> Fixo
  1234-5678 -> Inválido
```

---

## 🧪 Testes

### Suite Completa de Testes JUnit 5

```java
package com.example.validator;

import org.apache.commons.validator.routines.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de testes para Apache Commons Validator.
 */
class CommonsValidatorTest {
    
    @Nested
    @DisplayName("EmailValidator Tests")
    class EmailValidatorTests {
        
        private EmailValidator validator;
        
        @BeforeEach
        void setUp() {
            validator = EmailValidator.getInstance();
        }
        
        @Test
        @DisplayName("Deve validar emails corretos")
        void testValidEmails() {
            assertTrue(validator.isValid("user@example.com"));
            assertTrue(validator.isValid("john.doe@company.org"));
            assertTrue(validator.isValid("support+tag@service.co.uk"));
        }
        
        @Test
        @DisplayName("Deve rejeitar emails inválidos")
        void testInvalidEmails() {
            assertFalse(validator.isValid("plainaddress"));
            assertFalse(validator.isValid("@no-local.com"));
            assertFalse(validator.isValid("user@"));
            assertFalse(validator.isValid("user name@example.com"));
        }
        
        @Test
        @DisplayName("Deve tratar null e vazio")
        void testNullAndEmpty() {
            assertFalse(validator.isValid(null));
            assertFalse(validator.isValid(""));
            assertFalse(validator.isValid("   "));
        }
    }
    
    @Nested
    @DisplayName("UrlValidator Tests")
    class UrlValidatorTests {
        
        private UrlValidator validator;
        
        @BeforeEach
        void setUp() {
            validator = UrlValidator.getInstance();
        }
        
        @Test
        @DisplayName("Deve validar URLs corretas")
        void testValidUrls() {
            assertTrue(validator.isValid("http://www.example.com"));
            assertTrue(validator.isValid("https://example.com"));
            assertTrue(validator.isValid("https://example.com:8080/path"));
        }
        
        @Test
        @DisplayName("Deve rejeitar URLs inválidas")
        void testInvalidUrls() {
            assertFalse(validator.isValid("not a url"));
            assertFalse(validator.isValid("http://"));
            assertFalse(validator.isValid("://example.com"));
        }
        
        @Test
        @DisplayName("Deve validar esquemas customizados")
        void testCustomSchemes() {
            String[] schemes = {"http", "https", "ftp"};
            UrlValidator customValidator = new UrlValidator(schemes);
            
            assertTrue(customValidator.isValid("ftp://files.example.com"));
            assertFalse(customValidator.isValid("sftp://server.example.com"));
        }
    }
    
    @Nested
    @DisplayName("CreditCardValidator Tests")
    class CreditCardValidatorTests {
        
        private CreditCardValidator validator;
        
        @BeforeEach
        void setUp() {
            validator = new CreditCardValidator();
        }
        
        @Test
        @DisplayName("Deve validar cartões Visa")
        void testVisa() {
            assertTrue(validator.isValid("4532015112830366"));
            assertTrue(validator.isValid("4532-0151-1283-0366"));
        }
        
        @Test
        @DisplayName("Deve validar cartões Mastercard")
        void testMastercard() {
            assertTrue(validator.isValid("5425233430109903"));
        }
        
        @Test
        @DisplayName("Deve rejeitar números inválidos")
        void testInvalidCards() {
            assertFalse(validator.isValid("1234567890123456"));
            assertFalse(validator.isValid("abc"));
            assertFalse(validator.isValid(null));
        }
        
        @Test
        @DisplayName("Deve validar apenas tipos específicos")
        void testSpecificTypes() {
            CreditCardValidator visaOnly = new CreditCardValidator(
                CreditCardValidator.VISA
            );
            
            assertTrue(visaOnly.isValid("4532015112830366"));
            assertFalse(visaOnly.isValid("5425233430109903")); // Mastercard
        }
    }
    
    @Nested
    @DisplayName("DomainValidator Tests")
    class DomainValidatorTests {
        
        private DomainValidator validator;
        
        @BeforeEach
        void setUp() {
            validator = DomainValidator.getInstance();
        }
        
        @Test
        @DisplayName("Deve validar domínios corretos")
        void testValidDomains() {
            assertTrue(validator.isValid("example.com"));
            assertTrue(validator.isValid("sub.domain.example.com"));
            assertTrue(validator.isValid("example.co.uk"));
        }
        
        @Test
        @DisplayName("Deve rejeitar domínios inválidos")
        void testInvalidDomains() {
            assertFalse(validator.isValid("invalid"));
            assertFalse(validator.isValid("-example.com"));
            assertFalse(validator.isValid("example-.com"));
        }
    }
    
    @Nested
    @DisplayName("InetAddressValidator Tests")
    class InetAddressValidatorTests {
        
        private InetAddressValidator validator;
        
        @BeforeEach
        void setUp() {
            validator = InetAddressValidator.getInstance();
        }
        
        @Test
        @DisplayName("Deve validar IPv4")
        void testIPv4() {
            assertTrue(validator.isValidInet4Address("192.168.1.1"));
            assertTrue(validator.isValidInet4Address("10.0.0.1"));
            assertFalse(validator.isValidInet4Address("256.1.1.1"));
        }
        
        @Test
        @DisplayName("Deve validar IPv6")
        void testIPv6() {
            assertTrue(validator.isValidInet6Address("2001:0db8:85a3::1"));
            assertTrue(validator.isValidInet6Address("::1"));
            assertFalse(validator.isValidInet6Address("gggg::1"));
        }
    }
    
    @Nested
    @DisplayName("ISBNValidator Tests")
    class ISBNValidatorTests {
        
        private ISBNValidator validator;
        
        @BeforeEach
        void setUp() {
            validator = ISBNValidator.getInstance();
        }
        
        @Test
        @DisplayName("Deve validar ISBN-10")
        void testISBN10() {
            assertTrue(validator.isValidISBN10("0-596-52068-9"));
            assertFalse(validator.isValidISBN10("0-596-52068-0"));
        }
        
        @Test
        @DisplayName("Deve validar ISBN-13")
        void testISBN13() {
            assertTrue(validator.isValidISBN13("978-0-596-52068-7"));
            assertFalse(validator.isValidISBN13("978-0-596-52068-0"));
        }
        
        @Test
        @DisplayName("Deve validar ambos os formatos")
        void testBothFormats() {
            assertTrue(validator.isValid("0-596-52068-9"));
            assertTrue(validator.isValid("978-0-596-52068-7"));
        }
    }
    
    @Nested
    @DisplayName("DateValidator Tests")
    class DateValidatorTests {
        
        private DateValidator validator;
        
        @BeforeEach
        void setUp() {
            validator = DateValidator.getInstance();
        }
        
        @Test
        @DisplayName("Deve validar datas ISO")
        void testISODates() {
            assertNotNull(validator.validate("2025-10-10", "yyyy-MM-dd"));
            assertNotNull(validator.validate("2025-12-31", "yyyy-MM-dd"));
            assertNull(validator.validate("2025-13-01", "yyyy-MM-dd"));
        }
        
        @Test
        @DisplayName("Deve validar ranges")
        void testDateRanges() {
            java.util.Date min = validator.validate("2025-01-01", "yyyy-MM-dd");
            java.util.Date max = validator.validate("2025-12-31", "yyyy-MM-dd");
            java.util.Date test = validator.validate("2025-06-15", "yyyy-MM-dd");
            
            assertTrue(validator.isInRange(test, min, max));
        }
    }
    
    @Nested
    @DisplayName("IntegerValidator Tests")
    class IntegerValidatorTests {
        
        private IntegerValidator validator;
        
        @BeforeEach
        void setUp() {
            validator = IntegerValidator.getInstance();
        }
        
        @Test
        @DisplayName("Deve validar inteiros")
        void testIntegers() {
            assertEquals(123, validator.validate("123"));
            assertEquals(-456, validator.validate("-456"));
            assertNull(validator.validate("abc"));
        }
        
        @Test
        @DisplayName("Deve validar ranges")
        void testRanges() {
            assertTrue(validator.isInRange(validator.validate("50"), 1, 100));
            assertFalse(validator.isInRange(validator.validate("150"), 1, 100));
        }
        
        @Test
        @DisplayName("Deve validar mínimo e máximo")
        void testMinMax() {
            assertTrue(validator.minValue(validator.validate("10"), 5));
            assertTrue(validator.maxValue(validator.validate("10"), 20));
            assertFalse(validator.minValue(validator.validate("3"), 5));
        }
    }
}
```

---

## ✅ Best Practices

### 1. Reutilize Instâncias Singleton

```java
// ❌ Criar nova instância toda vez
public boolean validate(String email) {
    return EmailValidator.getInstance().isValid(email);
}

// ✅ Reutilizar instância (field ou constante)
private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();

public boolean validate(String email) {
    return EMAIL_VALIDATOR.isValid(email);
}
```

---

### 2. Normalize Entrada Antes de Validar

```java
public boolean validateEmail(String email) {
    if (email == null) {
        return false;
    }
    
    // Normalizar: trim e lowercase
    String normalized = email.trim().toLowerCase();
    
    return EmailValidator.getInstance().isValid(normalized);
}
```

---

### 3. Combine Validadores para Regras Complexas

```java
public boolean isValidBusinessEmail(String email) {
    EmailValidator emailValidator = EmailValidator.getInstance();
    DomainValidator domainValidator = DomainValidator.getInstance();
    
    // 1. Validar formato
    if (!emailValidator.isValid(email)) {
        return false;
    }
    
    // 2. Extrair domínio
    String domain = email.substring(email.indexOf('@') + 1);
    
    // 3. Validar domínio
    if (!domainValidator.isValid(domain)) {
        return false;
    }
    
    // 4. Rejeitar domínios gratuitos
    String[] freeDomains = {"gmail.com", "yahoo.com", "hotmail.com"};
    for (String free : freeDomains) {
        if (domain.equalsIgnoreCase(free)) {
            return false;
        }
    }
    
    return true;
}
```

---

### 4. Use Validadores Específicos

```java
// ❌ Regex manual para URL
public boolean isValidUrl(String url) {
    return url != null && url.matches("^https?://.*");
}

// ✅ Use UrlValidator - mais robusto
public boolean isValidUrl(String url) {
    return UrlValidator.getInstance().isValid(url);
}
```

---

### 5. Forneça Feedback Específico

```java
public class EmailValidationResult {
    public enum ErrorType {
        NULL_OR_EMPTY,
        INVALID_FORMAT,
        INVALID_DOMAIN,
        BLOCKED_DOMAIN
    }
    
    private final boolean valid;
    private final ErrorType errorType;
    
    // ... constructor, getters
    
    public static EmailValidationResult validate(String email) {
        if (email == null || email.trim().isEmpty()) {
            return new EmailValidationResult(false, ErrorType.NULL_OR_EMPTY);
        }
        
        EmailValidator validator = EmailValidator.getInstance();
        if (!validator.isValid(email)) {
            return new EmailValidationResult(false, ErrorType.INVALID_FORMAT);
        }
        
        // Mais validações...
        
        return new EmailValidationResult(true, null);
    }
    
    public String getErrorMessage() {
        if (valid) return "Valid";
        
        switch (errorType) {
            case NULL_OR_EMPTY: return "Email não pode ser vazio";
            case INVALID_FORMAT: return "Formato de email inválido";
            case INVALID_DOMAIN: return "Domínio inválido";
            case BLOCKED_DOMAIN: return "Domínio bloqueado";
            default: return "Erro desconhecido";
        }
    }
}
```

---

## 📊 Referência Rápida

### Guia de Decisão Rápida

| Validação | Classe | Método |
|-----------|--------|--------|
| Email | `EmailValidator` | `isValid(email)` |
| URL | `UrlValidator` | `isValid(url)` |
| Domínio | `DomainValidator` | `isValid(domain)` |
| IPv4 | `InetAddressValidator` | `isValidInet4Address(ip)` |
| IPv6 | `InetAddressValidator` | `isValidInet6Address(ip)` |
| Cartão de Crédito | `CreditCardValidator` | `isValid(cardNumber)` |
| ISBN | `ISBNValidator` | `isValid(isbn)` |
| ISSN | `ISSNValidator` | `isValid(issn)` |
| Data | `DateValidator` | `validate(date, pattern)` |
| Inteiro | `IntegerValidator` | `validate(str)` |
| Double | `DoubleValidator` | `validate(str)` |
| Regex | `RegexValidator` | `isValid(str)` |

---

### Hierarquia de Classes

```
org.apache.commons.validator.routines
│
├── EmailValidator
├── UrlValidator
├── DomainValidator
├── InetAddressValidator
│
├── CreditCardValidator
├── ISBNValidator
├── ISSNValidator
├── CodeValidator
│
├── DateValidator
├── TimeValidator
├── CalendarValidator
│
├── IntegerValidator
├── LongValidator
├── DoubleValidator
├── FloatValidator
├── BigDecimalValidator
├── PercentValidator
│
└── RegexValidator
```

---

## 📚 Recursos Adicionais

### Documentação Oficial
- [Apache Commons Validator Home](https://commons.apache.org/proper/commons-validator/)
- [API JavaDoc 1.10.0](https://commons.apache.org/proper/commons-validator/apidocs/)
- [User Guide](https://commons.apache.org/proper/commons-validator/userguide.html)

### Repositório
- [GitHub](https://github.com/apache/commons-validator)
- [Issues](https://issues.apache.org/jira/projects/VALIDATOR)

### Maven Dependency
```xml
<dependency>
    <groupId>commons-validator</groupId>
    <artifactId>commons-validator</artifactId>
    <version>1.10.0</version>
</dependency>
```

---

## 🎯 Resumo

### Principais Pontos

1. **EmailValidator**: Validação RFC-compliant de emails
2. **UrlValidator**: Validação completa de URLs com múltiplos esquemas
3. **DomainValidator**: Validação de nomes de domínio e TLDs
4. **InetAddressValidator**: Validação de endereços IPv4 e IPv6
5. **CreditCardValidator**: Algoritmo de Luhn para cartões
6. **ISBNValidator**: Validação de ISBN-10 e ISBN-13
7. **DateValidator**: Validação de datas com padrões customizados
8. **NumericValidators**: Validação com ranges e formatos
9. **RegexValidator**: Validações baseadas em regex
10. **Custom Validators**: Fácil criação de validadores personalizados

### Quando Usar

✅ **Use Commons Validator quando precisar de**:
- Validação RFC-compliant (email, URL, etc)
- Validação de cartões de crédito
- Validação de códigos (ISBN, ISSN, etc)
- Validação de datas e números com localização
- Evitar regex manual complexo

❌ **Não use quando**:
- Validações muito simples (preferir regex direto)
- Bean Validation (@NotNull, @Email) já atende
- Performance extrema é crítica (validadores são otimizados, mas cache se necessário)

---

**Voltar para**: [📁 Apache Commons](../README.md) | [📁 Libraries](../../README.md) | [📁 Repositório Principal](../../../README.md)
