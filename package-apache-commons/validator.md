# Apache Commons Validator - Documentação Completa

## 📚 Índice
1. [Introdução e Conceitos](#introdução-e-conceitos)
2. [Instalação e Setup](#instalação-e-setup)
3. [Validadores Built-in](#validadores-built-in)
4. [Email Validation](#email-validation)
5. [URL Validation](#url-validation)
6. [Domain Validation](#domain-validation)
7. [IP Address Validation](#ip-address-validation)
8. [Credit Card Validation](#credit-card-validation)
9. [Date and Time Validation](#date-and-time-validation)
10. [Numeric Validation](#numeric-validation)
11. [Regular Expression Validation](#regular-expression-validation)
12. [Custom Validators](#custom-validators)
13. [Validation Rules (XML)](#validation-rules-xml)
14. [Integration Patterns](#integration-patterns)
15. [Performance e Benchmarks](#performance-e-benchmarks)
16. [Melhores Práticas](#melhores-práticas)

---

## 🎯 Introdução e Conceitos

### O Que É Apache Commons Validator?

Apache Commons Validator é uma biblioteca Java que fornece um framework robusto e extensível para validação de dados. Ela oferece validadores prontos para uso comum (email, URL, cartão de crédito, etc.) e permite criar validações customizadas.

### Analogia do Mundo Real

Imagine um **porteiro de uma boate VIP**:
- **Email Validator** = Verifica se o convite está no formato correto
- **Credit Card Validator** = Valida se o cartão VIP é legítimo (Luhn algorithm)
- **URL Validator** = Confirma se o endereço do site está bem formado
- **Custom Validator** = Regras específicas do estabelecimento

### Arquitetura da Biblioteca

```
Apache Commons Validator
│
├── Validators (Validadores Prontos)
│   ├── EmailValidator
│   ├── UrlValidator
│   ├── DomainValidator
│   ├── InetAddressValidator
│   ├── CreditCardValidator
│   ├── ISBNValidator
│   ├── CodeValidator
│   └── RegexValidator
│
├── Routines (Validações de Rotina)
│   ├── DateValidator
│   ├── TimeValidator
│   ├── CalendarValidator
│   ├── IntegerValidator
│   ├── DoubleValidator
│   └── PercentValidator
│
└── Framework (XML-based)
    ├── Validator
    ├── ValidatorResources
    ├── ValidatorAction
    └── Field
```

### Quando Usar?

✅ **Use quando:**
- Validar entrada de usuários (formulários web)
- Validar dados de APIs externas
- Implementar regras de negócio de validação
- Garantir integridade de dados antes de persistir
- Validar formatos padrão (email, URL, etc.)

❌ **Não use quando:**
- Validações muito simples (preferir regex direto)
- Performance crítica em high-throughput (cache os validators)
- Validações de lógica de negócio complexa (preferir Bean Validation)

---

## ⚙️ Instalação e Setup

### Maven

```xml
<!-- https://mvnrepository.com/artifact/commons-validator/commons-validator -->
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

### Imports Principais

```java
// Validadores principais
import org.apache.commons.validator.routines.*;

// Email
import org.apache.commons.validator.routines.EmailValidator;

// URL
import org.apache.commons.validator.routines.UrlValidator;

// Domain e IP
import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;

// Credit Card
import org.apache.commons.validator.routines.CreditCardValidator;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;

// Date/Time
import org.apache.commons.validator.routines.DateValidator;
import org.apache.commons.validator.routines.TimeValidator;
import org.apache.commons.validator.routines.CalendarValidator;

// Numeric
import org.apache.commons.validator.routines.IntegerValidator;
import org.apache.commons.validator.routines.DoubleValidator;
import org.apache.commons.validator.routines.BigDecimalValidator;

// Regex e Code
import org.apache.commons.validator.routines.RegexValidator;
import org.apache.commons.validator.routines.CodeValidator;
import org.apache.commons.validator.routines.ISBNValidator;
```

---

## 📧 Email Validation

### Validação Básica

```java
import org.apache.commons.validator.routines.EmailValidator;

public class EmailValidationExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Email Validation ===\n");
        
        basicEmailValidation();
        advancedEmailValidation();
        localAddressValidation();
    }
    
    // Validação básica de email
    static void basicEmailValidation() {
        System.out.println("=== Validação Básica ===\n");
        
        // Obter instância singleton
        EmailValidator validator = EmailValidator.getInstance();
        
        // Emails válidos
        String[] validEmails = {
            "user@example.com",
            "john.doe@company.org",
            "support+tickets@service.co.uk",
            "admin_user@sub.domain.example.com",
            "user123@test-server.com"
        };
        
        System.out.println("Emails VÁLIDOS:");
        for (String email : validEmails) {
            boolean isValid = validator.isValid(email);
            System.out.println("  " + email + " -> " + isValid);
        }
        // Saída: todos true
        
        // Emails inválidos
        String[] invalidEmails = {
            "plainaddress",              // Sem @
            "@no-local-part.com",        // Sem parte local
            "missing-domain@.com",       // Domínio vazio
            "user@",                     // Sem domínio
            "user name@example.com",     // Espaço
            "user@domain@example.com",   // Múltiplos @
            "user..name@example.com",    // Pontos consecutivos
            ".user@example.com",         // Começa com ponto
            "user.@example.com"          // Termina com ponto
        };
        
        System.out.println("\nEmails INVÁLIDOS:");
        for (String email : invalidEmails) {
            boolean isValid = validator.isValid(email);
            System.out.println("  " + email + " -> " + isValid);
        }
        // Saída: todos false
    }
    
    // Validação avançada com opções
    static void advancedEmailValidation() {
        System.out.println("\n=== Validação Avançada ===\n");
        
        // Permitir endereços locais (sem domínio de TLD)
        EmailValidator localValidator = EmailValidator.getInstance(true);
        
        String localEmail = "user@localhost";
        
        System.out.println("Email: " + localEmail);
        System.out.println("Validator padrão: " + 
            EmailValidator.getInstance().isValid(localEmail));
        System.out.println("Validator local: " + 
            localValidator.isValid(localEmail));
        // Saída:
        // Email: user@localhost
        // Validator padrão: false
        // Validator local: true
        
        // Validar com domínios específicos permitidos
        String[] allowedDomains = {"example.com", "company.org"};
        
        String email1 = "user@example.com";
        String email2 = "user@other.com";
        
        boolean valid1 = EmailValidator.getInstance().isValid(email1) &&
                        isDomainAllowed(email1, allowedDomains);
        boolean valid2 = EmailValidator.getInstance().isValid(email2) &&
                        isDomainAllowed(email2, allowedDomains);
        
        System.out.println("\nValidação com domínios permitidos:");
        System.out.println(email1 + " -> " + valid1);  // true
        System.out.println(email2 + " -> " + valid2);  // false
    }
    
    static boolean isDomainAllowed(String email, String[] allowedDomains) {
        String domain = email.substring(email.indexOf("@") + 1);
        for (String allowed : allowedDomains) {
            if (domain.equalsIgnoreCase(allowed)) {
                return true;
            }
        }
        return false;
    }
    
    // Validação de endereços locais
    static void localAddressValidation() {
        System.out.println("\n=== Endereços Locais ===\n");
        
        EmailValidator standardValidator = EmailValidator.getInstance(false);
        EmailValidator localValidator = EmailValidator.getInstance(true);
        
        String[] localAddresses = {
            "admin@localhost",
            "user@intranet",
            "service@server"
        };
        
        for (String email : localAddresses) {
            System.out.println(email + ":");
            System.out.println("  Standard: " + standardValidator.isValid(email));
            System.out.println("  Local: " + localValidator.isValid(email));
        }
        // Saída:
        // admin@localhost:
        //   Standard: false
        //   Local: true
        // (etc.)
    }
    
    // Exemplo prático: Validação de formulário
    static class UserRegistrationForm {
        private String email;
        
        public boolean validate() {
            EmailValidator validator = EmailValidator.getInstance();
            
            if (email == null || email.trim().isEmpty()) {
                System.out.println("Email é obrigatório");
                return false;
            }
            
            if (!validator.isValid(email)) {
                System.out.println("Email inválido: " + email);
                return false;
            }
            
            // Validações adicionais
            if (email.length() > 254) { // RFC 5321
                System.out.println("Email muito longo");
                return false;
            }
            
            return true;
        }
    }
}
```

---

## 🌐 URL Validation

### Validação de URLs

```java
import org.apache.commons.validator.routines.UrlValidator;

public class UrlValidationExamples {
    
    public static void main(String[] args) {
        System.out.println("=== URL Validation ===\n");
        
        basicUrlValidation();
        schemeValidation();
        authorityValidation();
        customValidation();
    }
    
    // Validação básica de URL
    static void basicUrlValidation() {
        System.out.println("=== Validação Básica ===\n");
        
        // Validator padrão (http, https, ftp)
        UrlValidator validator = new UrlValidator();
        
        String[] validUrls = {
            "http://www.example.com",
            "https://example.com/path/to/resource",
            "http://example.com:8080/app",
            "https://sub.domain.example.com/page.html",
            "ftp://ftp.example.com/file.txt",
            "http://192.168.1.1",
            "https://example.com/search?q=test&lang=en"
        };
        
        System.out.println("URLs VÁLIDAS:");
        for (String url : validUrls) {
            System.out.println("  " + url + " -> " + validator.isValid(url));
        }
        // Saída: todos true
        
        String[] invalidUrls = {
            "not a url",
            "htp://example.com",        // Protocolo errado
            "http://",                  // Sem host
            "http:/example.com",        // Apenas uma barra
            "example.com",              // Sem protocolo
            "http://exam ple.com",      // Espaço
            "http://example..com",      // Pontos consecutivos
            "http://.example.com"       // Começa com ponto
        };
        
        System.out.println("\nURLs INVÁLIDAS:");
        for (String url : invalidUrls) {
            System.out.println("  " + url + " -> " + validator.isValid(url));
        }
        // Saída: todos false
    }
    
    // Validação de schemes específicos
    static void schemeValidation() {
        System.out.println("\n=== Validação de Schemes ===\n");
        
        // Apenas HTTP e HTTPS
        String[] schemes = {"http", "https"};
        UrlValidator httpValidator = new UrlValidator(schemes);
        
        System.out.println("Validator HTTP/HTTPS:");
        System.out.println("  http://example.com -> " + 
            httpValidator.isValid("http://example.com"));    // true
        System.out.println("  https://example.com -> " + 
            httpValidator.isValid("https://example.com"));   // true
        System.out.println("  ftp://example.com -> " + 
            httpValidator.isValid("ftp://example.com"));     // false
        
        // Schemes customizados
        String[] customSchemes = {"ws", "wss", "mqtt"};
        UrlValidator customValidator = new UrlValidator(customSchemes);
        
        System.out.println("\nValidator customizado (ws, wss, mqtt):");
        System.out.println("  ws://example.com -> " + 
            customValidator.isValid("ws://example.com"));    // true
        System.out.println("  mqtt://broker.com -> " + 
            customValidator.isValid("mqtt://broker.com"));   // true
        System.out.println("  http://example.com -> " + 
            customValidator.isValid("http://example.com"));  // false
    }
    
    // Validação de autoridade (host:port)
    static void authorityValidation() {
        System.out.println("\n=== Validação de Autoridade ===\n");
        
        // Permitir endereços locais
        UrlValidator validator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
        
        String[] urls = {
            "http://localhost:8080",
            "http://127.0.0.1:3000",
            "http://192.168.1.1",
            "http://intranet/app"
        };
        
        System.out.println("Com ALLOW_LOCAL_URLS:");
        for (String url : urls) {
            System.out.println("  " + url + " -> " + validator.isValid(url));
        }
        // Saída: todos true
        
        // Sem permitir locais
        UrlValidator strictValidator = new UrlValidator();
        
        System.out.println("\nSem ALLOW_LOCAL_URLS:");
        for (String url : urls) {
            System.out.println("  " + url + " -> " + strictValidator.isValid(url));
        }
        // Saída: localhost e intranet = false, IPs = true
    }
    
    // Validação customizada com flags
    static void customValidation() {
        System.out.println("\n=== Validação Customizada ===\n");
        
        // Combinar flags
        long options = UrlValidator.ALLOW_LOCAL_URLS | 
                      UrlValidator.ALLOW_2_SLASHES |
                      UrlValidator.NO_FRAGMENTS;
        
        UrlValidator validator = new UrlValidator(
            new String[]{"http", "https"}, 
            options
        );
        
        String[] testUrls = {
            "http://localhost/app",           // Local permitido
            "http://example.com//path",       // Barras duplas permitidas
            "http://example.com/page#anchor"  // Fragment não permitido
        };
        
        for (String url : testUrls) {
            System.out.println(url + " -> " + validator.isValid(url));
        }
        // Saída:
        // http://localhost/app -> true
        // http://example.com//path -> true
        // http://example.com/page#anchor -> false
    }
    
    // Flags disponíveis
    static void demonstrateFlags() {
        System.out.println("\n=== Flags Disponíveis ===\n");
        
        /*
         * UrlValidator.ALLOW_ALL_SCHEMES
         * - Permite qualquer scheme
         * 
         * UrlValidator.ALLOW_LOCAL_URLS
         * - Permite URLs locais (localhost, IPs privados)
         * 
         * UrlValidator.ALLOW_2_SLASHES
         * - Permite barras duplas no path
         * 
         * UrlValidator.NO_FRAGMENTS
         * - Não permite fragments (#anchor)
         */
        
        // Exemplo: Validador permissivo
        UrlValidator permissive = new UrlValidator(
            UrlValidator.ALLOW_ALL_SCHEMES | UrlValidator.ALLOW_LOCAL_URLS
        );
        
        System.out.println("Validador permissivo:");
        System.out.println("  custom://localhost/test -> " + 
            permissive.isValid("custom://localhost/test"));  // true
    }
    
    // Exemplo prático: Validação de configuração
    static class ApiConfiguration {
        private String baseUrl;
        private String webhookUrl;
        
        public boolean validate() {
            UrlValidator validator = new UrlValidator(new String[]{"http", "https"});
            
            if (baseUrl == null || !validator.isValid(baseUrl)) {
                System.out.println("Base URL inválida: " + baseUrl);
                return false;
            }
            
            // Webhook pode ser local em desenvolvimento
            UrlValidator webhookValidator = new UrlValidator(
                new String[]{"http", "https"},
                UrlValidator.ALLOW_LOCAL_URLS
            );
            
            if (webhookUrl != null && !webhookValidator.isValid(webhookUrl)) {
                System.out.println("Webhook URL inválida: " + webhookUrl);
                return false;
            }
            
            return true;
        }
    }
}
```

---

## 🌍 Domain Validation

### Validação de Domínios

```java
import org.apache.commons.validator.routines.DomainValidator;

public class DomainValidationExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Domain Validation ===\n");
        
        basicDomainValidation();
        tldValidation();
        customTldValidation();
    }
    
    // Validação básica de domínio
    static void basicDomainValidation() {
        System.out.println("=== Validação Básica ===\n");
        
        DomainValidator validator = DomainValidator.getInstance();
        
        String[] validDomains = {
            "example.com",
            "sub.example.com",
            "www.example.co.uk",
            "my-site.example.org",
            "test123.example.net",
            "deep.sub.domain.example.com"
        };
        
        System.out.println("Domínios VÁLIDOS:");
        for (String domain : validDomains) {
            System.out.println("  " + domain + " -> " + validator.isValid(domain));
        }
        // Saída: todos true
        
        String[] invalidDomains = {
            "example",              // Sem TLD
            ".example.com",         // Começa com ponto
            "example..com",         // Pontos consecutivos
            "example.com.",         // Termina com ponto
            "-example.com",         // Começa com hífen
            "example-.com",         // Termina com hífen
            "exam ple.com",         // Espaço
            "example.123"           // TLD numérico
        };
        
        System.out.println("\nDomínios INVÁLIDOS:");
        for (String domain : invalidDomains) {
            System.out.println("  " + domain + " -> " + validator.isValid(domain));
        }
        // Saída: todos false
    }
    
    // Validação de TLDs
    static void tldValidation() {
        System.out.println("\n=== Validação de TLDs ===\n");
        
        DomainValidator validator = DomainValidator.getInstance();
        
        // Verificar se TLD é válido
        String[] tlds = {"com", "org", "net", "br", "uk", "io", "dev", "xyz", "invalid"};
        
        System.out.println("TLDs válidos:");
        for (String tld : tlds) {
            System.out.println("  ." + tld + " -> " + validator.isValidTld(tld));
        }
        // Saída: invalid = false, outros = true
        
        // Tipos de TLDs
        System.out.println("\nTipos de TLDs:");
        
        // Generic TLDs
        System.out.println("Generic TLDs:");
        String[] genericTlds = {"com", "org", "net", "info", "biz"};
        for (String tld : genericTlds) {
            System.out.println("  " + tld + " -> " + validator.isValidGenericTld(tld));
        }
        
        // Country Code TLDs
        System.out.println("\nCountry Code TLDs:");
        String[] countryTlds = {"br", "us", "uk", "jp", "de", "fr"};
        for (String tld : countryTlds) {
            System.out.println("  " + tld + " -> " + validator.isValidCountryCodeTld(tld));
        }
        
        // Infrastructure TLD
        System.out.println("\nInfrastructure TLD:");
        System.out.println("  arpa -> " + validator.isValidInfrastructureTld("arpa"));
    }
    
    // TLDs customizados
    static void customTldValidation() {
        System.out.println("\n=== TLDs Customizados ===\n");
        
        // Obter instância modificável
        DomainValidator validator = DomainValidator.getInstance(true);
        
        // Adicionar TLD customizado (local)
        DomainValidator.Item customTld = new DomainValidator.Item(
            DomainValidator.ArrayType.GENERIC_PLUS,
            new String[]{"local", "internal", "test"}
        );
        
        // Atualizar validador
        validator.updateTLDOverride(
            DomainValidator.ArrayType.GENERIC_PLUS,
            new String[]{"local", "internal", "test"}
        );
        
        String[] customDomains = {
            "app.local",
            "server.internal",
            "api.test"
        };
        
        System.out.println("Domínios customizados:");
        for (String domain : customDomains) {
            System.out.println("  " + domain + " -> " + validator.isValid(domain));
        }
        // Saída: todos true com TLDs customizados
    }
    
    // Exemplo prático: Validação de domínios de email
    static class EmailDomainValidator {
        private final DomainValidator domainValidator;
        private final Set<String> blockedDomains;
        
        public EmailDomainValidator() {
            this.domainValidator = DomainValidator.getInstance();
            this.blockedDomains = new HashSet<>(Arrays.asList(
                "tempmail.com",
                "throwaway.email",
                "guerrillamail.com"
            ));
        }
        
        public boolean isValidEmailDomain(String email) {
            // Extrair domínio
            int atIndex = email.indexOf('@');
            if (atIndex < 0) {
                return false;
            }
            
            String domain = email.substring(atIndex + 1);
            
            // Validar formato
            if (!domainValidator.isValid(domain)) {
                System.out.println("Domínio com formato inválido: " + domain);
                return false;
            }
            
            // Verificar se está bloqueado
            if (blockedDomains.contains(domain.toLowerCase())) {
                System.out.println("Domínio bloqueado: " + domain);
                return false;
            }
            
            return true;
        }
    }
}
```

---

## 🔢 IP Address Validation

### Validação de Endereços IP

```java
import org.apache.commons.validator.routines.InetAddressValidator;

public class IpAddressValidationExamples {
    
    public static void main(String[] args) {
        System.out.println("=== IP Address Validation ===\n");
        
        ipv4Validation();
        ipv6Validation();
        combinedValidation();
    }
    
    // Validação IPv4
    static void ipv4Validation() {
        System.out.println("=== Validação IPv4 ===\n");
        
        InetAddressValidator validator = InetAddressValidator.getInstance();
        
        String[] validIpv4 = {
            "192.168.1.1",
            "10.0.0.1",
            "172.16.0.1",
            "127.0.0.1",
            "8.8.8.8",
            "255.255.255.255",
            "0.0.0.0"
        };
        
        System.out.println("IPv4 VÁLIDOS:");
        for (String ip : validIpv4) {
            System.out.println("  " + ip + " -> " + validator.isValidInet4Address(ip));
        }
        // Saída: todos true
        
        String[] invalidIpv4 = {
            "256.1.1.1",         // Octeto > 255
            "192.168.1",         // Faltando octeto
            "192.168.1.1.1",     // Octeto extra
            "192.168.-1.1",      // Negativo
            "192.168.1.a",       // Não numérico
            "192.168..1",        // Ponto duplo
            " 192.168.1.1",      // Espaço
            "192.168.1.1 "       // Espaço
        };
        
        System.out.println("\nIPv4 INVÁLIDOS:");
        for (String ip : invalidIpv4) {
            System.out.println("  " + ip + " -> " + validator.isValidInet4Address(ip));
        }
        // Saída: todos false
    }
    
    // Validação IPv6
    static void ipv6Validation() {
        System.out.println("\n=== Validação IPv6 ===\n");
        
        InetAddressValidator validator = InetAddressValidator.getInstance();
        
        String[] validIpv6 = {
            "2001:0db8:85a3:0000:0000:8a2e:0370:7334",  // Formato completo
            "2001:db8:85a3::8a2e:370:7334",             // Comprimido
            "::1",                                       // Loopback
            "::",                                        // Todos zeros
            "fe80::1",                                   // Link-local
            "::ffff:192.168.1.1",                       // IPv4-mapped
            "2001:db8::1"                                // Comprimido
        };
        
        System.out.println("IPv6 VÁLIDOS:");
        for (String ip : validIpv6) {
            System.out.println("  " + ip + " -> " + validator.isValidInet6Address(ip));
        }
        // Saída: todos true
        
        String[] invalidIpv6 = {
            "02001:0db8:0000:0000:0000:ff00:0042:8329",  // Grupo > 4 chars
            "2001:0db8::8a2e::7334",                     // Dupla compressão
            "gggg::1",                                   // Hex inválido
            "::ffff:999.168.1.1",                        // IPv4 inválido
            "2001:db8"                                   // Incompleto
        };
        
        System.out.println("\nIPv6 INVÁLIDOS:");
        for (String ip : invalidIpv6) {
            System.out.println("  " + ip + " -> " + validator.isValidInet6Address(ip));
        }
        // Saída: todos false
    }
    
    // Validação combinada
    static void combinedValidation() {
        System.out.println("\n=== Validação Combinada ===\n");
        
        InetAddressValidator validator = InetAddressValidator.getInstance();
        
        String[] addresses = {
            "192.168.1.1",                              // IPv4
            "2001:db8::1",                              // IPv6
            "::ffff:192.168.1.1",                       // IPv4-mapped IPv6
            "example.com",                              // Não é IP
            "256.1.1.1"                                 // IPv4 inválido
        };
        
        for (String addr : addresses) {
            boolean valid = validator.isValid(addr);
            boolean isV4 = validator.isValidInet4Address(addr);
            boolean isV6 = validator.isValidInet6Address(addr);
            
            System.out.println(addr + ":");
            System.out.println("  Válido: " + valid);
            System.out.println("  IPv4: " + isV4);
            System.out.println("  IPv6: " + isV6);
        }
        // Saída:
        // 192.168.1.1:
        //   Válido: true
        //   IPv4: true
        //   IPv6: false
        // (etc.)
    }
    
    // Exemplo prático: Validação de rede
    static class NetworkConfiguration {
        private String ipAddress;
        private String gateway;
        private String dnsServer;
        
        public boolean validate() {
            InetAddressValidator validator = InetAddressValidator.getInstance();
            
            // Validar IP
            if (ipAddress == null || !validator.isValidInet4Address(ipAddress)) {
                System.out.println("IP inválido: " + ipAddress);
                return false;
            }
            
            // Validar Gateway
            if (gateway == null || !validator.isValidInet4Address(gateway)) {
                System.out.println("Gateway inválido: " + gateway);
                return false;
            }
            
            // Validar DNS (pode ser IPv4 ou IPv6)
            if (dnsServer == null || !validator.isValid(dnsServer)) {
                System.out.println("DNS inválido: " + dnsServer);
                return false;
            }
            
            // Validar mesma rede (simplificado)
            if (!isSameNetwork(ipAddress, gateway)) {
                System.out.println("IP e Gateway em redes diferentes");
                return false;
            }
            
            return true;
        }
        
        private boolean isSameNetwork(String ip1, String ip2) {
            // Simplificado: verifica os 3 primeiros octetos
            String[] parts1 = ip1.split("\\.");
            String[] parts2 = ip2.split("\\.");
            
            return parts1[0].equals(parts2[0]) &&
                   parts1[1].equals(parts2[1]) &&
                   parts1[2].equals(parts2[2]);
        }
    }
}
```

---

## 💳 Credit Card Validation

### Validação de Cartões de Crédito

```java
import org.apache.commons.validator.routines.CreditCardValidator;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;

public class CreditCardValidationExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Credit Card Validation ===\n");
        
        basicCardValidation();
        specificCardTypes();
        luhnAlgorithm();
        practicalExample();
    }
    
    // Validação básica
    static void basicCardValidation() {
        System.out.println("=== Validação Básica ===\n");
        
        // Validator que aceita todos os tipos comuns
        CreditCardValidator validator = new CreditCardValidator();
        
        // Números de teste (válidos pelo algoritmo de Luhn)
        String[] testCards = {
            "4532015112830366",      // Visa (16 dígitos)
            "5425233430109903",      // MasterCard
            "374245455400126",       // American Express (15 dígitos)
            "6011000991001201",      // Discover
            "3566002020360505",      // JCB
            "30569309025904",        // Diners Club (14 dígitos)
            "6304000000000000"       // Maestro
        };
        
        System.out.println("Cartões de teste:");
        for (String card : testCards) {
            boolean valid = validator.isValid(card);
            System.out.println("  " + card + " -> " + valid);
        }
        // Saída: todos true
        
        // Cartões inválidos
        String[] invalidCards = {
            "1234567890123456",      // Falha no Luhn
            "4532-0151-1283-0366",   // Com hífens (não aceito)
            "4532 0151 1283 0366",   // Com espaços (não aceito)
            "453201511283036",       // Faltando dígito
            "45320151128303666"      // Dígito extra
        };
        
        System.out.println("\nCartões inválidos:");
        for (String card : invalidCards) {
            boolean valid = validator.isValid(card);
            System.out.println("  " + card + " -> " + valid);
        }
        // Saída: todos false
    }
    
    // Validação de tipos específicos
    static void specificCardTypes() {
        System.out.println("\n=== Tipos Específicos ===\n");
        
        // Apenas Visa
        CreditCardValidator visaOnly = new CreditCardValidator(
            CreditCardValidator.VISA
        );
        
        // Apenas Visa e MasterCard
        CreditCardValidator visaMaster = new CreditCardValidator(
            CreditCardValidator.VISA | CreditCardValidator.MASTERCARD
        );
        
        // Todos exceto American Express
        CreditCardValidator noAmex = new CreditCardValidator(
            CreditCardValidator.VISA |
            CreditCardValidator.MASTERCARD |
            CreditCardValidator.DISCOVER
        );
        
        String visaCard = "4532015112830366";
        String masterCard = "5425233430109903";
        String amexCard = "374245455400126";
        
        System.out.println("Validator Visa-only:");
        System.out.println("  Visa: " + visaOnly.isValid(visaCard));          // true
        System.out.println("  Master: " + visaOnly.isValid(masterCard));      // false
        System.out.println("  Amex: " + visaOnly.isValid(amexCard));          // false
        
        System.out.println("\nValidator Visa+MasterCard:");
        System.out.println("  Visa: " + visaMaster.isValid(visaCard));        // true
        System.out.println("  Master: " + visaMaster.isValid(masterCard));    // true
        System.out.println("  Amex: " + visaMaster.isValid(amexCard));        // false
        
        // Validar e obter tipo
        CreditCardValidator fullValidator = new CreditCardValidator();
        
        System.out.println("\nValidar e detectar tipo:");
        String[] cards = {visaCard, masterCard, amexCard};
        for (String card : cards) {
            Object result = fullValidator.validate(card);
            if (result != null) {
                System.out.println("  " + card + " -> Válido, tipo: " + result);
            }
        }
        // Saída:
        //   4532015112830366 -> Válido, tipo: Visa
        //   5425233430109903 -> Válido, tipo: Mastercard
        //   374245455400126 -> Válido, tipo: Amex
    }
    
    // Algoritmo de Luhn
    static void luhnAlgorithm() {
        System.out.println("\n=== Algoritmo de Luhn ===\n");
        
        LuhnCheckDigit luhn = new LuhnCheckDigit();
        
        // Validar dígito verificador
        String cardNumber = "453201511283036";  // Sem o último dígito
        
        try {
            // Calcular dígito verificador
            String checkDigit = luhn.calculate(cardNumber);
            System.out.println("Número: " + cardNumber);
            System.out.println("Dígito verificador: " + checkDigit);
            System.out.println("Número completo: " + cardNumber + checkDigit);
            // Saída:
            // Número: 453201511283036
            // Dígito verificador: 6
            // Número completo: 4532015112830366
            
            // Validar número completo
            String fullNumber = cardNumber + checkDigit;
            boolean valid = luhn.isValid(fullNumber);
            System.out.println("Válido: " + valid);  // true
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        
        // Demonstrar passo a passo
        demonstrateLuhnSteps("4532015112830366");
    }
    
    static void demonstrateLuhnSteps(String cardNumber) {
        System.out.println("\n=== Passo a Passo do Luhn ===\n");
        System.out.println("Número: " + cardNumber);
        
        // Reverter e processar
        char[] digits = new StringBuilder(cardNumber).reverse().toString().toCharArray();
        
        int sum = 0;
        System.out.println("\nProcessamento:");
        for (int i = 0; i < digits.length; i++) {
            int digit = Character.getNumericValue(digits[i]);
            
            if (i % 2 == 1) {  // Posições ímpares (após reverter)
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
                System.out.printf("Posição %d: %c -> dobrado = %d\n", 
                    i, digits[i], digit);
            } else {
                System.out.printf("Posição %d: %c -> mantido = %d\n", 
                    i, digits[i], digit);
            }
            
            sum += digit;
        }
        
        System.out.println("\nSoma total: " + sum);
        System.out.println("Válido (soma % 10 == 0): " + (sum % 10 == 0));
    }
    
    // Exemplo prático
    static void practicalExample() {
        System.out.println("\n=== Exemplo Prático: Checkout ===\n");
        
        PaymentProcessor processor = new PaymentProcessor();
        
        // Simulação de checkout
        String[] testPayments = {
            "4532015112830366",      // Visa válido
            "5425233430109903",      // MasterCard válido
            "1234567890123456",      // Inválido
            "374245455400126"        // Amex (se não aceito)
        };
        
        for (String card : testPayments) {
            processor.processPayment(card, 150.00);
        }
    }
    
    static class PaymentProcessor {
        // Aceita apenas Visa e MasterCard
        private final CreditCardValidator validator = new CreditCardValidator(
            CreditCardValidator.VISA | CreditCardValidator.MASTERCARD
        );
        
        public boolean processPayment(String cardNumber, double amount) {
            System.out.println("\n--- Processando Pagamento ---");
            System.out.println("Cartão: " + maskCardNumber(cardNumber));
            System.out.println("Valor: R$ " + String.format("%.2f", amount));
            
            // Remover espaços e hífens
            String cleanCard = cardNumber.replaceAll("[\\s-]", "");
            
            // Validar
            Object cardType = validator.validate(cleanCard);
            
            if (cardType == null) {
                System.out.println("Status: REJEITADO - Cartão inválido");
                return false;
            }
            
            System.out.println("Tipo: " + cardType);
            System.out.println("Status: APROVADO");
            return true;
        }
        
        private String maskCardNumber(String cardNumber) {
            if (cardNumber.length() < 4) {
                return "****";
            }
            return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
        }
    }
}
```

---

## 📅 Date and Time Validation

### Validação de Data e Hora

```java
import org.apache.commons.validator.routines.*;
import java.util.*;
import java.text.*;
import java.time.*;

public class DateTimeValidationExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Date and Time Validation ===\n");
        
        dateValidation();
        timeValidation();
        calendarValidation();
        customFormats();
        rangeValidation();
    }
    
    // Validação de datas
    static void dateValidation() {
        System.out.println("=== Date Validation ===\n");
        
        DateValidator validator = DateValidator.getInstance();
        
        // Formato padrão (locale dependente)
        String[] dates = {
            "12/25/2024",        // MM/dd/yyyy (US)
            "25/12/2024",        // dd/MM/yyyy (BR)
            "2024-12-25"         // yyyy-MM-dd (ISO)
        };
        
        // Validar com formato específico
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");
        
        System.out.println("Formato MM/dd/yyyy:");
        Date date1 = validator.validate(dates[0], format1);
        System.out.println("  " + dates[0] + " -> " + (date1 != null));  // true
        
        System.out.println("\nFormato dd/MM/yyyy:");
        Date date2 = validator.validate(dates[1], format2);
        System.out.println("  " + dates[1] + " -> " + (date2 != null));  // true
        
        System.out.println("\nFormato yyyy-MM-dd:");
        Date date3 = validator.validate(dates[2], format3);
        System.out.println("  " + dates[2] + " -> " + (date3 != null));  // true
        
        // Validação com pattern
        System.out.println("\nValidação com pattern:");
        String[] testDates = {
            "2024-12-25",
            "2024-13-01",        // Mês inválido
            "2024-02-30",        // Dia inválido
            "2024-12-32"         // Dia inválido
        };
        
        for (String dateStr : testDates) {
            Date validated = validator.validate(dateStr, "yyyy-MM-dd");
            System.out.println("  " + dateStr + " -> " + (validated != null));
        }
        // Saída:
        // 2024-12-25 -> true
        // 2024-13-01 -> false
        // 2024-02-30 -> false
        // 2024-12-32 -> false
    }
    
    // Validação de hora
    static void timeValidation() {
        System.out.println("\n=== Time Validation ===\n");
        
        TimeValidator validator = TimeValidator.getInstance();
        
        // Formatos de hora
        String[] times = {
            "14:30:00",          // HH:mm:ss
            "2:30:00 PM",        // h:mm:ss a
            "14:30",             // HH:mm
            "23:59:59"           // HH:mm:ss
        };
        
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("h:mm:ss a");
        SimpleDateFormat format3 = new SimpleDateFormat("HH:mm");
        
        System.out.println("Formato HH:mm:ss:");
        Date time1 = validator.validate(times[0], format1);
        System.out.println("  " + times[0] + " -> " + (time1 != null));
        
        System.out.println("\nFormato h:mm:ss a:");
        Date time2 = validator.validate(times[1], format2);
        System.out.println("  " + times[1] + " -> " + (time2 != null));
        
        // Validação de horas inválidas
        String[] invalidTimes = {
            "25:00:00",          // Hora > 23
            "14:60:00",          // Minuto > 59
            "14:30:60",          // Segundo > 59
            "99:99:99"           // Tudo inválido
        };
        
        System.out.println("\nHoras inválidas:");
        for (String time : invalidTimes) {
            Date validated = validator.validate(time, "HH:mm:ss");
            System.out.println("  " + time + " -> " + (validated != null));
        }
        // Saída: todos false
    }
    
    // Validação de calendário
    static void calendarValidation() {
        System.out.println("\n=== Calendar Validation ===\n");
        
        CalendarValidator validator = CalendarValidator.getInstance();
        
        // Validar e converter para Calendar
        String dateStr = "2024-12-25";
        Calendar calendar = validator.validate(dateStr, "yyyy-MM-dd");
        
        if (calendar != null) {
            System.out.println("Data válida: " + dateStr);
            System.out.println("Ano: " + calendar.get(Calendar.YEAR));
            System.out.println("Mês: " + (calendar.get(Calendar.MONTH) + 1));
            System.out.println("Dia: " + calendar.get(Calendar.DAY_OF_MONTH));
            System.out.println("Dia da semana: " + 
                getDayOfWeekName(calendar.get(Calendar.DAY_OF_WEEK)));
        }
        // Saída:
        // Data válida: 2024-12-25
        // Ano: 2024
        // Mês: 12
        // Dia: 25
        // Dia da semana: Wednesday
        
        // Validar com timezone
        TimeZone utcZone = TimeZone.getTimeZone("UTC");
        TimeZone spZone = TimeZone.getTimeZone("America/Sao_Paulo");
        
        Calendar utcCal = validator.validate(dateStr, "yyyy-MM-dd", utcZone);
        Calendar spCal = validator.validate(dateStr, "yyyy-MM-dd", spZone);
        
        System.out.println("\nCom timezone:");
        System.out.println("UTC: " + utcCal.getTimeZone().getID());
        System.out.println("São Paulo: " + spCal.getTimeZone().getID());
    }
    
    // Formatos customizados
    static void customFormats() {
        System.out.println("\n=== Formatos Customizados ===\n");
        
        DateValidator validator = DateValidator.getInstance();
        
        // Brasileiro: dd/MM/yyyy
        SimpleDateFormat brFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        // Americano: MM/dd/yyyy
        SimpleDateFormat usFormat = new SimpleDateFormat("MM/dd/yyyy");
        
        // ISO: yyyy-MM-dd
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        // Extenso: dd 'de' MMMM 'de' yyyy
        SimpleDateFormat extFormat = new SimpleDateFormat(
            "dd 'de' MMMM 'de' yyyy", 
            new Locale("pt", "BR")
        );
        
        String brDate = "25/12/2024";
        String usDate = "12/25/2024";
        String isoDate = "2024-12-25";
        String extDate = "25 de dezembro de 2024";
        
        System.out.println("Formato BR (dd/MM/yyyy):");
        System.out.println("  " + brDate + " -> " + 
            (validator.validate(brDate, brFormat) != null));
        
        System.out.println("\nFormato US (MM/dd/yyyy):");
        System.out.println("  " + usDate + " -> " + 
            (validator.validate(usDate, usFormat) != null));
        
        System.out.println("\nFormato ISO (yyyy-MM-dd):");
        System.out.println("  " + isoDate + " -> " + 
            (validator.validate(isoDate, isoFormat) != null));
        
        System.out.println("\nFormato Extenso:");
        System.out.println("  " + extDate + " -> " + 
            (validator.validate(extDate, extFormat) != null));
    }
    
    // Validação de intervalo
    static void rangeValidation() {
        System.out.println("\n=== Validação de Intervalo ===\n");
        
        DateValidator validator = DateValidator.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        
        try {
            // Definir intervalo permitido
            Date minDate = format.parse("2024-01-01");
            Date maxDate = format.parse("2024-12-31");
            
            String[] testDates = {
                "2023-12-31",    // Antes do mínimo
                "2024-01-01",    // Exatamente no mínimo
                "2024-06-15",    // Dentro do intervalo
                "2024-12-31",    // Exatamente no máximo
                "2025-01-01"     // Depois do máximo
            };
            
            System.out.println("Intervalo permitido: 2024-01-01 a 2024-12-31\n");
            
            for (String dateStr : testDates) {
                Date date = validator.validate(dateStr, format);
                
                if (date == null) {
                    System.out.println(dateStr + " -> Formato inválido");
                    continue;
                }
                
                boolean inRange = !date.before(minDate) && !date.after(maxDate);
                System.out.println(dateStr + " -> " + 
                    (inRange ? "DENTRO" : "FORA") + " do intervalo");
            }
            // Saída:
            // 2023-12-31 -> FORA do intervalo
            // 2024-01-01 -> DENTRO do intervalo
            // 2024-06-15 -> DENTRO do intervalo
            // 2024-12-31 -> DENTRO do intervalo
            // 2025-01-01 -> FORA do intervalo
            
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    // Helpers
    static String getDayOfWeekName(int dayOfWeek) {
        String[] days = {"", "Sunday", "Monday", "Tuesday", "Wednesday", 
                        "Thursday", "Friday", "Saturday"};
        return days[dayOfWeek];
    }
    
    // Exemplo prático: Validação de reserva
    static class BookingValidator {
        private final DateValidator dateValidator = DateValidator.getInstance();
        private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        
        public boolean validateBookingDate(String dateStr) {
            // Validar formato
            Date bookingDate = dateValidator.validate(dateStr, format);
            if (bookingDate == null) {
                System.out.println("Data com formato inválido: " + dateStr);
                return false;
            }
            
            // Não pode ser no passado
            Date today = new Date();
            if (bookingDate.before(today)) {
                System.out.println("Data não pode ser no passado: " + dateStr);
                return false;
            }
            
            // Máximo 1 ano no futuro
            Calendar maxDate = Calendar.getInstance();
            maxDate.add(Calendar.YEAR, 1);
            
            if (bookingDate.after(maxDate.getTime())) {
                System.out.println("Data muito distante (máx 1 ano): " + dateStr);
                return false;
            }
            
            // Não permite domingos
            Calendar cal = Calendar.getInstance();
            cal.setTime(bookingDate);
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                System.out.println("Não aceitamos reservas aos domingos");
                return false;
            }
            
            return true;
        }
    }
}
```

```

---

## 🔢 Numeric Validation

### Validação de Números

```java
import org.apache.commons.validator.routines.*;
import java.math.BigDecimal;
import java.util.Locale;

public class NumericValidationExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Numeric Validation ===\n");
        
        integerValidation();
        doubleValidation();
        bigDecimalValidation();
        percentValidation();
        currencyValidation();
    }
    
    // Validação de inteiros
    static void integerValidation() {
        System.out.println("=== Integer Validation ===\n");
        
        IntegerValidator validator = IntegerValidator.getInstance();
        
        String[] validIntegers = {
            "123",
            "-456",
            "0",
            "999999"
        };
        
        System.out.println("Inteiros VÁLIDOS:");
        for (String num : validIntegers) {
            Integer value = validator.validate(num);
            System.out.println("  " + num + " -> " + value);
        }
        // Saída:
        // 123 -> 123
        // -456 -> -456
        // 0 -> 0
        // 999999 -> 999999
        
        String[] invalidIntegers = {
            "123.45",        // Decimal
            "abc",           // Não numérico
            "1,234",         // Com vírgula
            "1e5",           // Notação científica
            " 123 "          // Com espaços
        };
        
        System.out.println("\nInteiros INVÁLIDOS:");
        for (String num : invalidIntegers) {
            Integer value = validator.validate(num);
            System.out.println("  " + num + " -> " + value);
        }
        // Saída: todos null
        
        // Validação com intervalo
        System.out.println("\nValidação com intervalo (0-100):");
        String[] testNumbers = {"50", "0", "100", "101", "-1"};
        
        for (String num : testNumbers) {
            Integer value = validator.validate(num);
            boolean inRange = value != null && 
                            validator.isInRange(value, 0, 100);
            System.out.println("  " + num + " -> " + inRange);
        }
        // Saída:
        // 50 -> true
        // 0 -> true
        // 100 -> true
        // 101 -> false
        // -1 -> false
    }
    
    // Validação de double
    static void doubleValidation() {
        System.out.println("\n=== Double Validation ===\n");
        
        DoubleValidator validator = DoubleValidator.getInstance();
        
        String[] validDoubles = {
            "123.45",
            "-456.78",
            "0.0",
            "999.999",
            "1e5",           // Notação científica
            "1.5e-3"
        };
        
        System.out.println("Doubles VÁLIDOS:");
        for (String num : validDoubles) {
            Double value = validator.validate(num);
            System.out.println("  " + num + " -> " + value);
        }
        // Saída:
        // 123.45 -> 123.45
        // -456.78 -> -456.78
        // 0.0 -> 0.0
        // 999.999 -> 999.999
        // 1e5 -> 100000.0
        // 1.5e-3 -> 0.0015
        
        // Validação com locale
        DoubleValidator brValidator = DoubleValidator.getInstance();
        
        String brNumber = "1.234,56";  // Formato brasileiro
        String usNumber = "1,234.56";  // Formato americano
        
        Locale brLocale = new Locale("pt", "BR");
        Locale usLocale = Locale.US;
        
        System.out.println("\nCom locale:");
        System.out.println("BR (" + brNumber + "): " + 
            brValidator.validate(brNumber, brLocale));
        System.out.println("US (" + usNumber + "): " + 
            brValidator.validate(usNumber, usLocale));
        // Saída:
        // BR (1.234,56): 1234.56
        // US (1,234.56): 1234.56
        
        // Validação de intervalo
        System.out.println("\nValidação com intervalo (0.0-100.0):");
        String[] testNumbers = {"50.5", "0.0", "100.0", "100.1", "-0.1"};
        
        for (String num : testNumbers) {
            Double value = validator.validate(num);
            boolean inRange = value != null && 
                            validator.isInRange(value, 0.0, 100.0);
            System.out.println("  " + num + " -> " + inRange);
        }
    }
    
    // Validação de BigDecimal
    static void bigDecimalValidation() {
        System.out.println("\n=== BigDecimal Validation ===\n");
        
        BigDecimalValidator validator = BigDecimalValidator.getInstance();
        
        String[] testNumbers = {
            "123456789012345678901234567890.123456789",  // Muito grande
            "-999999999999999999999.999999999",
            "0.000000000000000001"
        };
        
        System.out.println("BigDecimal (precisão alta):");
        for (String num : testNumbers) {
            BigDecimal value = validator.validate(num);
            System.out.println("  " + num);
            System.out.println("    -> " + value);
        }
        
        // Validação monetária
        Locale brLocale = new Locale("pt", "BR");
        
        String moneyValue = "R$ 1.234,56";
        // Remover prefixo e usar locale
        String cleanValue = moneyValue.replace("R$ ", "");
        BigDecimal money = validator.validate(cleanValue, brLocale);
        
        System.out.println("\nValor monetário:");
        System.out.println("  Input: " + moneyValue);
        System.out.println("  Parsed: " + money);
        // Saída:
        // Input: R$ 1.234,56
        // Parsed: 1234.56
    }
    
    // Validação de percentual
    static void percentValidation() {
        System.out.println("\n=== Percent Validation ===\n");
        
        PercentValidator validator = PercentValidator.getInstance();
        
        String[] percentages = {
            "50%",
            "100%",
            "0%",
            "99.99%"
        };
        
        System.out.println("Percentuais:");
        for (String pct : percentages) {
            BigDecimal value = validator.validate(pct);
            System.out.println("  " + pct + " -> " + value);
        }
        // Saída:
        // 50% -> 0.50
        // 100% -> 1.00
        // 0% -> 0.00
        // 99.99% -> 0.9999
    }
    
    // Validação de moeda
    static void currencyValidation() {
        System.out.println("\n=== Currency Validation ===\n");
        
        CurrencyValidator validator = CurrencyValidator.getInstance();
        
        // Com locale brasileiro
        Locale brLocale = new Locale("pt", "BR");
        
        String[] brCurrencies = {
            "R$ 1.234,56",
            "R$ 10,00",
            "R$ 0,99"
        };
        
        System.out.println("Moeda brasileira:");
        for (String curr : brCurrencies) {
            BigDecimal value = validator.validate(curr, brLocale);
            System.out.println("  " + curr + " -> " + value);
        }
        
        // Com locale americano
        Locale usLocale = Locale.US;
        
        String[] usCurrencies = {
            "$1,234.56",
            "$10.00",
            "$0.99"
        };
        
        System.out.println("\nMoeda americana:");
        for (String curr : usCurrencies) {
            BigDecimal value = validator.validate(curr, usLocale);
            System.out.println("  " + curr + " -> " + value);
        }
    }
    
    // Exemplo prático: Validação de produto
    static class ProductValidator {
        private final IntegerValidator intValidator = IntegerValidator.getInstance();
        private final DoubleValidator doubleValidator = DoubleValidator.getInstance();
        private final BigDecimalValidator decimalValidator = BigDecimalValidator.getInstance();
        
        public boolean validateProduct(String quantityStr, String priceStr) {
            // Validar quantidade (inteiro positivo)
            Integer quantity = intValidator.validate(quantityStr);
            if (quantity == null || quantity <= 0) {
                System.out.println("Quantidade inválida: " + quantityStr);
                return false;
            }
            
            if (quantity > 9999) {
                System.out.println("Quantidade muito alta (máx: 9999)");
                return false;
            }
            
            // Validar preço (decimal positivo)
            BigDecimal price = decimalValidator.validate(priceStr);
            if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Preço inválido: " + priceStr);
                return false;
            }
            
            if (price.compareTo(new BigDecimal("999999.99")) > 0) {
                System.out.println("Preço muito alto (máx: 999999.99)");
                return false;
            }
            
            // Calcular total
            BigDecimal total = price.multiply(new BigDecimal(quantity));
            System.out.println("Validação OK:");
            System.out.println("  Quantidade: " + quantity);
            System.out.println("  Preço unitário: R$ " + price);
            System.out.println("  Total: R$ " + total);
            
            return true;
        }
    }
}
```

---

## 🔤 Regular Expression Validation

### Validação com Regex

```java
import org.apache.commons.validator.routines.RegexValidator;

public class RegexValidationExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Regular Expression Validation ===\n");
        
        basicRegex();
        multiplePatterns();
        caseInsensitive();
        captureGroups();
        practicalExamples();
    }
    
    // Regex básico
    static void basicRegex() {
        System.out.println("=== Regex Básico ===\n");
        
        // CPF brasileiro: 000.000.000-00
        RegexValidator cpfValidator = new RegexValidator(
            "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$"
        );
        
        String[] cpfs = {
            "123.456.789-00",    // Válido
            "111.222.333-44",    // Válido
            "12345678900",       // Sem formatação
            "123.456.789",       // Incompleto
            "abc.def.ghi-jk"     // Não numérico
        };
        
        System.out.println("Validação de CPF:");
        for (String cpf : cpfs) {
            boolean valid = cpfValidator.isValid(cpf);
            System.out.println("  " + cpf + " -> " + valid);
        }
        // Saída:
        // 123.456.789-00 -> true
        // 111.222.333-44 -> true
        // 12345678900 -> false
        // 123.456.789 -> false
        // abc.def.ghi-jk -> false
        
        // Telefone brasileiro: (00) 00000-0000 ou (00) 0000-0000
        RegexValidator phoneValidator = new RegexValidator(
            "^\\(\\d{2}\\) \\d{4,5}-\\d{4}$"
        );
        
        String[] phones = {
            "(11) 98765-4321",   // Celular
            "(11) 3456-7890",    // Fixo
            "11987654321",       // Sem formatação
            "(11) 8765-4321"     // Válido (8 dígitos)
        };
        
        System.out.println("\nValidação de Telefone:");
        for (String phone : phones) {
            boolean valid = phoneValidator.isValid(phone);
            System.out.println("  " + phone + " -> " + valid);
        }
    }
    
    // Múltiplos padrões
    static void multiplePatterns() {
        System.out.println("\n=== Múltiplos Padrões ===\n");
        
        // Aceita CPF com ou sem formatação
        RegexValidator cpfValidator = new RegexValidator(
            new String[] {
                "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$",  // Com formatação
                "^\\d{11}$"                            // Sem formatação
            }
        );
        
        String[] cpfs = {
            "123.456.789-00",    // Formatado
            "12345678900",       // Sem formatação
            "123.456.789",       // Inválido
            "123456789000"       // Muitos dígitos
        };
        
        System.out.println("CPF (com ou sem formatação):");
        for (String cpf : cpfs) {
            boolean valid = cpfValidator.isValid(cpf);
            System.out.println("  " + cpf + " -> " + valid);
        }
        // Saída:
        // 123.456.789-00 -> true
        // 12345678900 -> true
        // 123.456.789 -> false
        // 123456789000 -> false
    }
    
    // Case insensitive
    static void caseInsensitive() {
        System.out.println("\n=== Case Insensitive ===\n");
        
        // Username: letras, números, underscore, hífen (4-20 chars)
        RegexValidator usernameValidator = new RegexValidator(
            "^[a-z0-9_-]{4,20}$",
            false  // case insensitive
        );
        
        String[] usernames = {
            "john_doe",          // Válido
            "JOHN_DOE",          // Válido (case insensitive)
            "John-Doe",          // Válido (case insensitive)
            "abc",               // Muito curto
            "this_is_a_very_long_username",  // Muito longo
            "user@name"          // Caractere inválido
        };
        
        System.out.println("Username (case insensitive):");
        for (String username : usernames) {
            boolean valid = usernameValidator.isValid(username);
            System.out.println("  " + username + " -> " + valid);
        }
    }
    
    // Capture groups
    static void captureGroups() {
        System.out.println("\n=== Capture Groups ===\n");
        
        // Data: dd/MM/yyyy
        RegexValidator dateValidator = new RegexValidator(
            "^(\\d{2})/(\\d{2})/(\\d{4})$"
        );
        
        String date = "25/12/2024";
        
        // Validar e extrair grupos
        String[] groups = dateValidator.match(date);
        
        if (groups != null) {
            System.out.println("Data: " + date);
            System.out.println("  Completa: " + groups[0]);  // Match completo
            System.out.println("  Dia: " + groups[1]);
            System.out.println("  Mês: " + groups[2]);
            System.out.println("  Ano: " + groups[3]);
        }
        // Saída:
        // Data: 25/12/2024
        //   Completa: 25/12/2024
        //   Dia: 25
        //   Mês: 12
        //   Ano: 2024
        
        // Placa de carro brasileira (Mercosul): ABC1D23
        RegexValidator plateValidator = new RegexValidator(
            "^([A-Z]{3})(\\d)([A-Z])(\\d{2})$",
            false
        );
        
        String plate = "ABC1D23";
        groups = plateValidator.match(plate);
        
        if (groups != null) {
            System.out.println("\nPlaca: " + plate);
            System.out.println("  Letras: " + groups[1]);
            System.out.println("  Número: " + groups[2]);
            System.out.println("  Letra: " + groups[3]);
            System.out.println("  Números: " + groups[4]);
        }
    }
    
    // Exemplos práticos
    static void practicalExamples() {
        System.out.println("\n=== Exemplos Práticos ===\n");
        
        // CEP brasileiro: 00000-000
        RegexValidator cepValidator = new RegexValidator(
            new String[] {
                "^\\d{5}-\\d{3}$",  // Com hífen
                "^\\d{8}$"          // Sem hífen
            }
        );
        
        // CNPJ brasileiro: 00.000.000/0000-00
        RegexValidator cnpjValidator = new RegexValidator(
            new String[] {
                "^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$",  // Formatado
                "^\\d{14}$"                                   // Sem formatação
            }
        );
        
        // Senha forte: mín 8 chars, 1 maiúscula, 1 minúscula, 1 número, 1 especial
        RegexValidator passwordValidator = new RegexValidator(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
        );
        
        // Testes
        System.out.println("CEP:");
        System.out.println("  01234-567 -> " + cepValidator.isValid("01234-567"));
        System.out.println("  01234567 -> " + cepValidator.isValid("01234567"));
        
        System.out.println("\nCNPJ:");
        System.out.println("  12.345.678/0001-90 -> " + 
            cnpjValidator.isValid("12.345.678/0001-90"));
        System.out.println("  12345678000190 -> " + 
            cnpjValidator.isValid("12345678000190"));
        
        System.out.println("\nSenha:");
        System.out.println("  Pass@123 -> " + 
            passwordValidator.isValid("Pass@123"));
        System.out.println("  password -> " + 
            passwordValidator.isValid("password"));
        System.out.println("  PASSWORD -> " + 
            passwordValidator.isValid("PASSWORD"));
        System.out.println("  Pass1234 -> " + 
            passwordValidator.isValid("Pass1234"));
    }
}
```

---

## 🛠️ Custom Validators

### Criando Validadores Customizados

```java
import org.apache.commons.validator.routines.checkdigit.*;
import org.apache.commons.validator.routines.*;

public class CustomValidatorExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Custom Validators ===\n");
        
        cpfValidator();
        cnpjValidator();
        customCodeValidator();
    }
    
    // Validador de CPF completo
    static void cpfValidator() {
        System.out.println("=== Validador de CPF ===\n");
        
        CPFValidator validator = new CPFValidator();
        
        String[] cpfs = {
            "123.456.789-09",    // Válido (formato e dígitos)
            "111.111.111-11",    // Inválido (todos iguais)
            "123.456.789-00",    // Inválido (dígito errado)
            "12345678909",       // Válido (sem formatação)
            "123.456.789"        // Inválido (incompleto)
        };
        
        for (String cpf : cpfs) {
            boolean valid = validator.isValid(cpf);
            System.out.println(cpf + " -> " + valid);
        }
    }
    
    // Validador de CNPJ completo
    static void cnpjValidator() {
        System.out.println("\n=== Validador de CNPJ ===\n");
        
        CNPJValidator validator = new CNPJValidator();
        
        String[] cnpjs = {
            "11.222.333/0001-81",  // Válido
            "11222333000181",      // Válido (sem formatação)
            "11.222.333/0001-00",  // Inválido (dígito errado)
            "11.222.333/0001"      // Inválido (incompleto)
        };
        
        for (String cnpj : cnpjs) {
            boolean valid = validator.isValid(cnpj);
            System.out.println(cnpj + " -> " + valid);
        }
    }
    
    // CodeValidator customizado
    static void customCodeValidator() {
        System.out.println("\n=== Code Validator Customizado ===\n");
        
        // Validador de ISBN-10
        CheckDigit isbnCheck = new ISBNCheckDigit();
        RegexValidator isbnRegex = new RegexValidator("^\\d{9}[0-9X]$");
        CodeValidator isbnValidator = new CodeValidator(isbnRegex, isbnCheck);
        
        String[] isbns = {
            "0306406152",    // Válido
            "0306406151",    // Inválido (check digit)
            "030640615X",    // Válido (X = 10)
            "123456789"      // Inválido (incompleto)
        };
        
        System.out.println("ISBN-10:");
        for (String isbn : isbns) {
            Object result = isbnValidator.validate(isbn);
            System.out.println("  " + isbn + " -> " + (result != null));
        }
    }
}

// Validador de CPF customizado
class CPFValidator {
    private final RegexValidator formatValidator;
    
    public CPFValidator() {
        this.formatValidator = new RegexValidator(
            new String[] {
                "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$",
                "^\\d{11}$"
            }
        );
    }
    
    public boolean isValid(String cpf) {
        // Validar formato
        if (!formatValidator.isValid(cpf)) {
            return false;
        }
        
        // Remover formatação
        String cleanCpf = cpf.replaceAll("[.-]", "");
        
        // Rejeitar CPFs com todos dígitos iguais
        if (cleanCpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        // Validar dígitos verificadores
        return validateCheckDigits(cleanCpf);
    }
    
    private boolean validateCheckDigits(String cpf) {
        // Primeiro dígito verificador
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int firstDigit = 11 - (sum % 11);
        if (firstDigit > 9) firstDigit = 0;
        
        if (Character.getNumericValue(cpf.charAt(9)) != firstDigit) {
            return false;
        }
        
        // Segundo dígito verificador
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int secondDigit = 11 - (sum % 11);
        if (secondDigit > 9) secondDigit = 0;
        
        return Character.getNumericValue(cpf.charAt(10)) == secondDigit;
    }
}

// Validador de CNPJ customizado
class CNPJValidator {
    private final RegexValidator formatValidator;
    
    public CNPJValidator() {
        this.formatValidator = new RegexValidator(
            new String[] {
                "^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$",
                "^\\d{14}$"
            }
        );
    }
    
    public boolean isValid(String cnpj) {
        if (!formatValidator.isValid(cnpj)) {
            return false;
        }
        
        String cleanCnpj = cnpj.replaceAll("[./-]", "");
        
        if (cleanCnpj.matches("(\\d)\\1{13}")) {
            return false;
        }
        
        return validateCheckDigits(cleanCnpj);
    }
    
    private boolean validateCheckDigits(String cnpj) {
        // Primeiro dígito
        int[] weight1 = {5,4,3,2,9,8,7,6,5,4,3,2};
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += Character.getNumericValue(cnpj.charAt(i)) * weight1[i];
        }
        int digit1 = sum % 11 < 2 ? 0 : 11 - (sum % 11);
        
        if (Character.getNumericValue(cnpj.charAt(12)) != digit1) {
            return false;
        }
        
        // Segundo dígito
        int[] weight2 = {6,5,4,3,2,9,8,7,6,5,4,3,2};
        sum = 0;
        for (int i = 0; i < 13; i++) {
            sum += Character.getNumericValue(cnpj.charAt(i)) * weight2[i];
        }
        int digit2 = sum % 11 < 2 ? 0 : 11 - (sum % 11);
        
        return Character.getNumericValue(cnpj.charAt(13)) == digit2;
    }
}
```

---

## ⚙️ Integration Patterns

### Padrões de Integração

```java
import org.apache.commons.validator.routines.*;
import java.util.*;

public class IntegrationPatternExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Integration Patterns ===\n");
        
        validationChain();
        validationResult();
        validatorFacade();
    }
    
    // Cadeia de validação
    static void validationChain() {
        System.out.println("=== Validation Chain ===\n");
        
        UserValidator validator = new UserValidator();
        
        User user1 = new User(
            "john.doe@example.com",
            "http://example.com",
            "123.456.789-09"
        );
        
        ValidationResult result = validator.validate(user1);
        
        System.out.println("Usuário 1:");
        System.out.println("  Válido: " + result.isValid());
        if (!result.isValid()) {
            System.out.println("  Erros:");
            result.getErrors().forEach(err -> 
                System.out.println("    - " + err)
            );
        }
        
        User user2 = new User(
            "invalid-email",
            "not-a-url",
            "000.000.000-00"
        );
        
        result = validator.validate(user2);
        
        System.out.println("\nUsuário 2:");
        System.out.println("  Válido: " + result.isValid());
        if (!result.isValid()) {
            System.out.println("  Erros:");
            result.getErrors().forEach(err -> 
                System.out.println("    - " + err)
            );
        }
    }
    
    // Resultado de validação
    static void validationResult() {
        System.out.println("\n=== Validation Result ===\n");
        
        FormValidator validator = new FormValidator();
        
        Map<String, String> formData = new HashMap<>();
        formData.put("email", "user@example.com");
        formData.put("age", "25");
        formData.put("website", "https://example.com");
        
        ValidationResult result = validator.validateForm(formData);
        
        if (result.isValid()) {
            System.out.println("Formulário válido!");
        } else {
            System.out.println("Erros no formulário:");
            result.getFieldErrors().forEach((field, errors) -> {
                System.out.println("  " + field + ":");
                errors.forEach(error -> 
                    System.out.println("    - " + error)
                );
            });
        }
    }
    
    // Facade de validadores
    static void validatorFacade() {
        System.out.println("\n=== Validator Facade ===\n");
        
        ValidationFacade facade = ValidationFacade.getInstance();
        
        System.out.println("Email:");
        System.out.println("  user@example.com -> " + 
            facade.validateEmail("user@example.com"));
        System.out.println("  invalid -> " + 
            facade.validateEmail("invalid"));
        
        System.out.println("\nURL:");
        System.out.println("  https://example.com -> " + 
            facade.validateUrl("https://example.com"));
        System.out.println("  not-url -> " + 
            facade.validateUrl("not-url"));
        
        System.out.println("\nCPF:");
        System.out.println("  123.456.789-09 -> " + 
            facade.validateCPF("123.456.789-09"));
        System.out.println("  000.000.000-00 -> " + 
            facade.validateCPF("000.000.000-00"));
    }
}

// Classes auxiliares
class User {
    private String email;
    private String website;
    private String cpf;
    
    public User(String email, String website, String cpf) {
        this.email = email;
        this.website = website;
        this.cpf = cpf;
    }
    
    public String getEmail() { return email; }
    public String getWebsite() { return website; }
    public String getCpf() { return cpf; }
}

class ValidationResult {
    private boolean valid = true;
    private List<String> errors = new ArrayList<>();
    private Map<String, List<String>> fieldErrors = new HashMap<>();
    
    public void addError(String error) {
        this.valid = false;
        this.errors.add(error);
    }
    
    public void addFieldError(String field, String error) {
        this.valid = false;
        this.fieldErrors
            .computeIfAbsent(field, k -> new ArrayList<>())
            .add(error);
    }
    
    public boolean isValid() { return valid; }
    public List<String> getErrors() { return errors; }
    public Map<String, List<String>> getFieldErrors() { return fieldErrors; }
}

class UserValidator {
    private final EmailValidator emailValidator = EmailValidator.getInstance();
    private final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
    private final CPFValidator cpfValidator = new CPFValidator();
    
    public ValidationResult validate(User user) {
        ValidationResult result = new ValidationResult();
        
        // Validar email
        if (user.getEmail() == null || !emailValidator.isValid(user.getEmail())) {
            result.addError("Email inválido: " + user.getEmail());
        }
        
        // Validar website
        if (user.getWebsite() == null || !urlValidator.isValid(user.getWebsite())) {
            result.addError("Website inválido: " + user.getWebsite());
        }
        
        // Validar CPF
        if (user.getCpf() == null || !cpfValidator.isValid(user.getCpf())) {
            result.addError("CPF inválido: " + user.getCpf());
        }
        
        return result;
    }
}

class FormValidator {
    private final EmailValidator emailValidator = EmailValidator.getInstance();
    private final IntegerValidator intValidator = IntegerValidator.getInstance();
    private final UrlValidator urlValidator = new UrlValidator();
    
    public ValidationResult validateForm(Map<String, String> data) {
        ValidationResult result = new ValidationResult();
        
        // Email
        String email = data.get("email");
        if (email == null || email.isEmpty()) {
            result.addFieldError("email", "Email é obrigatório");
        } else if (!emailValidator.isValid(email)) {
            result.addFieldError("email", "Email com formato inválido");
        }
        
        // Age
        String ageStr = data.get("age");
        if (ageStr == null || ageStr.isEmpty()) {
            result.addFieldError("age", "Idade é obrigatória");
        } else {
            Integer age = intValidator.validate(ageStr);
            if (age == null) {
                result.addFieldError("age", "Idade deve ser um número");
            } else if (age < 18 || age > 120) {
                result.addFieldError("age", "Idade deve estar entre 18 e 120");
            }
        }
        
        // Website (opcional)
        String website = data.get("website");
        if (website != null && !website.isEmpty() && !urlValidator.isValid(website)) {
            result.addFieldError("website", "Website com formato inválido");
        }
        
        return result;
    }
}

class ValidationFacade {
    private static final ValidationFacade INSTANCE = new ValidationFacade();
    
    private final EmailValidator emailValidator;
    private final UrlValidator urlValidator;
    private final CPFValidator cpfValidator;
    
    private ValidationFacade() {
        this.emailValidator = EmailValidator.getInstance();
        this.urlValidator = new UrlValidator();
        this.cpfValidator = new CPFValidator();
    }
    
    public static ValidationFacade getInstance() {
        return INSTANCE;
    }
    
    public boolean validateEmail(String email) {
        return emailValidator.isValid(email);
    }
    
    public boolean validateUrl(String url) {
        return urlValidator.isValid(url);
    }
    
    public boolean validateCPF(String cpf) {
        return cpfValidator.isValid(cpf);
    }
}
```

---

## 📊 Performance e Benchmarks

### Otimização e Performance

```java
public class PerformanceExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Performance ===\n");
        
        singletonVsNew();
        caching();
        bulkValidation();
    }
    
    // Singleton vs new instance
    static void singletonVsNew() {
        System.out.println("=== Singleton vs New Instance ===\n");
        
        int iterations = 1_000_000;
        
        // Singleton (recomendado)
        long start = System.currentTimeMillis();
        EmailValidator singleton = EmailValidator.getInstance();
        for (int i = 0; i < iterations; i++) {
            singleton.isValid("test@example.com");
        }
        long singletonTime = System.currentTimeMillis() - start;
        
        System.out.println("Singleton (" + iterations + " iterações): " + 
            singletonTime + "ms");
        
        // Nova instância (NÃO recomendado)
        start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            EmailValidator.getInstance().isValid("test@example.com");
        }
        long newInstanceTime = System.currentTimeMillis() - start;
        
        System.out.println("Chamando getInstance() toda vez: " + 
            newInstanceTime + "ms");
        
        System.out.println("\n💡 Use singleton: " + 
            ((newInstanceTime - singletonTime) * 100 / singletonTime) + 
            "% mais rápido");
    }
    
    // Cache de resultados
    static void caching() {
        System.out.println("\n=== Caching ===\n");
        
        CachedValidator validator = new CachedValidator();
        
        String[] emails = {
            "user1@example.com",
            "user2@example.com",
            "user1@example.com",  // Repetido
            "user3@example.com",
            "user2@example.com"   // Repetido
        };
        
        for (String email : emails) {
            boolean valid = validator.isValidEmail(email);
            System.out.println(email + " -> " + valid);
        }
        
        System.out.println("\nEstatísticas do cache:");
        System.out.println("  Hits: " + validator.getCacheHits());
        System.out.println("  Misses: " + validator.getCacheMisses());
        System.out.println("  Hit rate: " + 
            (validator.getCacheHits() * 100.0 / emails.length) + "%");
    }
    
    // Validação em lote
    static void bulkValidation() {
        System.out.println("\n=== Bulk Validation ===\n");
        
        List<String> emails = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            emails.add("user" + i + "@example.com");
        }
        
        BulkValidator validator = new BulkValidator();
        
        long start = System.currentTimeMillis();
        Map<String, Boolean> results = validator.validateEmails(emails);
        long time = System.currentTimeMillis() - start;
        
        long validCount = results.values().stream()
            .filter(v -> v)
            .count();
        
        System.out.println("Validação de " + emails.size() + " emails:");
        System.out.println("  Tempo: " + time + "ms");
        System.out.println("  Válidos: " + validCount);
        System.out.println("  Throughput: " + 
            (emails.size() * 1000 / time) + " emails/segundo");
    }
}

class CachedValidator {
    private final EmailValidator emailValidator = EmailValidator.getInstance();
    private final Map<String, Boolean> cache = new HashMap<>();
    private int cacheHits = 0;
    private int cacheMisses = 0;
    
    public boolean isValidEmail(String email) {
        if (cache.containsKey(email)) {
            cacheHits++;
            return cache.get(email);
        }
        
        cacheMisses++;
        boolean valid = emailValidator.isValid(email);
        cache.put(email, valid);
        return valid;
    }
    
    public int getCacheHits() { return cacheHits; }
    public int getCacheMisses() { return cacheMisses; }
}

class BulkValidator {
    private final EmailValidator emailValidator = EmailValidator.getInstance();
    
    public Map<String, Boolean> validateEmails(List<String> emails) {
        Map<String, Boolean> results = new HashMap<>();
        
        // Processamento sequencial
        for (String email : emails) {
            results.put(email, emailValidator.isValid(email));
        }
        
        return results;
    }
    
    // Versão paralela para grandes volumes
    public Map<String, Boolean> validateEmailsParallel(List<String> emails) {
        return emails.parallelStream()
            .collect(HashMap::new,
                    (map, email) -> map.put(email, emailValidator.isValid(email)),
                    HashMap::putAll);
    }
}
```

---

## ✅ Melhores Práticas

### Guidelines de Uso

```markdown
## 🎯 Melhores Práticas

### 1. Use Singleton Validators
```java
// ✅ BOM - Reutilizar instância
private final EmailValidator emailValidator = EmailValidator.getInstance();

// ❌ RUIM - Criar nova instância toda vez
if (EmailValidator.getInstance().isValid(email)) { }
```

### 2. Cache Resultados Quando Possível
```java
// Para validações repetidas
Map<String, Boolean> validationCache = new HashMap<>();
```

### 3. Valide Cedo e Retorne Rápido
```java
// Validar formato antes de lógica complexa
if (email == null || !emailValidator.isValid(email)) {
    return ValidationResult.invalid("Email inválido");
}

// Agora pode prosseguir com lógica mais pesada
```

### 4. Combine Validadores
```java
// Validação completa em uma classe
class ContactValidator {
    private final EmailValidator emailValidator;
    private final DomainValidator domainValidator;
    private final UrlValidator urlValidator;
    
    public ValidationResult validate(Contact contact) {
        // Validar múltiplos campos
    }
}
```

### 5. Mensagens de Erro Claras
```java
// ❌ Genérico
throw new ValidationException("Inválido");

// ✅ Específico
throw new ValidationException("Email inválido: formato incorreto");
```

### 6. Validação em Layers
```java
// Layer 1: Formato (Apache Commons Validator)
if (!emailValidator.isValid(email)) {
    return false;
}

// Layer 2: Regras de negócio
if (isBlacklistedDomain(email)) {
    return false;
}

// Layer 3: Verificação externa (DNS, etc.)
if (!emailExists(email)) {
    return false;
}
```

### 7. Thread Safety
```java
// Validators são thread-safe
// Pode usar em ambiente multi-thread
private static final EmailValidator VALIDATOR = EmailValidator.getInstance();
```

### 8. Locale Awareness
```java
// Use locale correto para números/moedas
Locale brLocale = new Locale("pt", "BR");
DoubleValidator validator = DoubleValidator.getInstance();
Double value = validator.validate("1.234,56", brLocale);
```

### 9. Fail Fast
```java
public void registerUser(UserDTO dto) {
    // Validar TUDO antes de processar
    ValidationResult result = validator.validate(dto);
    if (!result.isValid()) {
        throw new ValidationException(result.getErrors());
    }
    
    // Agora processar com confiança
    userService.save(dto);
}
```

### 10. Documentar Regras
```java
/**
 * Valida CPF brasileiro.
 * 
 * Regras:
 * - Formato: 000.000.000-00 ou 00000000000
 * - Não aceita sequências (111.111.111-11)
 * - Valida dígitos verificadores
 * 
 * @param cpf CPF a validar
 * @return true se válido
 */
public boolean validateCPF(String cpf) {
    // implementação
}
```
```

---

## 📚 Referências

- [Apache Commons Validator - Site Oficial](https://commons.apache.org/proper/commons-validator/)
- [Javadoc](https://commons.apache.org/proper/commons-validator/apidocs/)
- [User Guide](https://commons.apache.org/proper/commons-validator/userguide.html)
- [Maven Repository](https://mvnrepository.com/artifact/commons-validator/commons-validator)

---

## 📦 Dependências Maven

```xml
<!-- Apache Commons Validator -->
<dependency>
    <groupId>commons-validator</groupId>
    <artifactId>commons-validator</artifactId>
    <version>1.10.0</version>
</dependency>

<!-- Dependências transitivas -->
<!-- Apache Commons Beanutils (incluído automaticamente) -->
<!-- Apache Commons Logging (incluído automaticamente) -->
```

---

*Apache Commons Validator fornece validação robusta e testada em batalha para aplicações Java enterprise, cobrindo desde validações simples até regras complexas de negócio.*