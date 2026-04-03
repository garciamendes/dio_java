# 💰 Sistema Bancário em Java

Projeto desenvolvido como atividade prática do bootcamp **Accenture - Desenvolvimento Java & Cloud** na DIO.

## 🚀 Objetivo

Simular operações básicas de um sistema bancário para praticar fundamentos de Java, lógica de programação e organização de código, além de introduzir conceitos de regras de negócio.

---

## 🧩 Funcionalidades

- 💰 Depósito
- 💸 Saque
- 🏦 Cheque especial com limite
- 📄 Extrato de transações
- 📊 Controle de saldo e saldo devedor

---

## ⚙️ Regras de Negócio

- O limite do cheque especial é liberado após o primeiro depósito
- Para valores:
    - Até R$ 500 → limite fixo
    - Acima de R$ 500 → 50% do valor depositado
- Uso do cheque especial possui taxa de 20%
- Todas as operações são registradas no extrato

---

## 🧱 Estrutura do Projeto
src/
├── dtos/ # Objetos de transferência de dados (records)
├── entities/ # Regras de negócio (ex: Bank)
├── Enums/ # Tipos e status de transações
└── Main.java # Execução do sistema

---

## ▶️ Como executar

1. Clone o repositório: git clone https://github.com/garciamendes/dio_java.git
2. Acesse a pasta do projeto: cd dio_java
3. Execute o projeto pela sua IDE (IntelliJ, Eclipse, VS Code) ou via terminal

---

## 📌 Exemplo de saída
Valor depositado: 521.0
Limite disponível de cheque especial: 260.5
Valor retirado: 130.0
Saldo devedor do cheque especial: 0.0

Tipo: Depósito | Valor: R$ 521.0 | Status: Sucesso
Tipo: Saque | Valor: R$ 130.0 | Status: Sucesso
Tipo: Cheque especial | Valor: R$ 270.0 | Status: Falhou

---

## 🛠️ Tecnologias utilizadas

- Java
- Programação orientada a objetos
- Records (Java moderno)
- Collections (Map, List)

---

## 📈 Próximas melhorias

- [ ] Uso de `BigDecimal` para valores monetários
- [ ] Separação em camadas (Service, Domain)
- [ ] Testes unitários (JUnit)
- [ ] Transformar em API REST (Spring Boot)
- [ ] Persistência com banco de dados

---

## 📚 Contexto

Projeto desenvolvido como prática durante o bootcamp da Accenture na DIO, com foco em evolução contínua em desenvolvimento backend.

---

## 👨‍💻 Autor

Matheus Garcia  
https://github.com/garciamendes