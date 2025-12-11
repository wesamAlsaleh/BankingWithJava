# 🏦 BankingWithJava

This repository contains the solution for **Project 1** of the **GA Java Bootcamp** (November 2025). The project is a
simulation of a basic banking system implemented in Java, focusing on core object-oriented programming (OOP) principles
and file handling for persistence.

## 🌟 Features

* **Account Management:** Create, read, update, and delete bank accounts.
* **Transaction Processing:** Support for `DEPOSIT`, `WITHDRAW` and `TRANSFER` transactions.
* **Overdraft Protection:** Implements specific business rules for handling overdrafts, including fees and account
  deactivation.
* **Card Management:** Logic to read and validate debit card information.
* **File Persistence:** All account and transaction data are stored in files within a simulated database directory
  structure.
* **Currency Conversion:** Handles transactions across different currencies with a simulated exchange rate service.

## 🚀 Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing
purposes.

### Prerequisites

You will need the following software installed on your machine:

* **Java Development Kit (JDK):** Version 17 or higher (Recommended).
* **An IDE:** IntelliJ IDEA.

### Installation

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/wesamAlsaleh/BankingWithJava.git
   ```
2. **Navigate to the Project Directory:**
   ```bash
   cd BankingWithJava
   ```
3. **Run the Main Application:**
   Locate the main class (`BankSystem.java`) and run it from your IDE or the command line.

## 📂 Project Structure

The key components of the project are organized as follows:

| Directory/File       | Description                                                                              |
|:---------------------|:-----------------------------------------------------------------------------------------|
| `src/`               | Contains all the source code for the banking application.                                |
| `Entity Classes`     | **Model:** Holds classes like `Account`, `DebitCard`, `Transaction`.                     |
| `Service Classes`    | **Service:** Contains the business logic (e.g., `AccountService`, `TransactionService`). |
| `Repository Classes` | **Repository:** Handles persistence and file I/O operations.                             |
| `db/`                | Simulated database folder where account and card files are stored.                       |

## 📐 Business Rules Implemented

The project successfully implements the following core business requirements:

* **Overdraft Fee:** A fixed **$35 USD** fee is charged for any overdraft transaction, converted to the account's local
  currency.
* **Negative Balance Limit:** Withdrawals exceeding the equivalent of **$100 USD** are prohibited if the account's
  current balance is already negative.
* **Account Deactivation:** An account is automatically **deactivated** after reaching **2 overdrafts**.
* **Reactivation:** The system includes logic to reactivate a deactivated account once the customer resolves the
  negative balance (by depositing funds to bring the balance to $\ge 0$).

## 💡 Development Process
This section documents the planning, tracking, and iterative development approach used to complete the project.

**Planning and Tracking**

* All features, tasks, and bugs were managed using Atlassian Jira.
* Jira allowed for clear task prioritization, scope management, and transparent progress tracking.
* Jira Board Link (User Stories and Planning): [Jira Board Link](https://wesamalsaleh23-1764554049477.atlassian.net/jira/software/projects/GA01/boards/1?atlOrigin=eyJpIjoiNmU3NDJkNGYxMjg0NDg0NWEyNWY2NmM4YmI0MzA0NWEiLCJwIjoiaiJ9)



---

## 🏗️ System Design: Entity Relationship Diagram (ERD)

The following diagram illustrates the key entities (tables/data models) in the banking system and the relationships
between them.

![Entity Relationship Diagram (ERD)](https://github.com/wesamAlsaleh/BankingWithJava/blob/main/assets/ERD.png)

---

## 📸 Screenshots

Here are some screenshots showcasing the user interface and key functionalities of the banking application:

### 1. Application startup and Login

| Description      | Screenshot                                                                                                                               |
|:-----------------|:-----------------------------------------------------------------------------------------------------------------------------------------|
| **Startup Page** | ![Startup Page](https://github.com/wesamAlsaleh/BankingWithJava/blob/improvement/refactor-db-paths/assets/screenshots/start-up-page.png) |
| **Login Page**   | ![Login Page](https://github.com/wesamAlsaleh/BankingWithJava/blob/improvement/refactor-db-paths/assets/screenshots/login-page.png)      |

### 2. User Dashboards and Transactions

| Description                        | Screenshot                                                                                                                                                             |
|:-----------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Customer Homepage**              | ![Customer Homepage](https://github.com/wesamAlsaleh/BankingWithJava/blob/improvement/refactor-db-paths/assets/screenshots/customer-page.png)                          |
| **See All Accounts**               | ![See All Accounts](https://github.com/wesamAlsaleh/BankingWithJava/blob/improvement/refactor-db-paths/assets/screenshots/account-page.png)                            |
| **Deposit Page**                   | ![Deposit Page](https://github.com/wesamAlsaleh/BankingWithJava/blob/improvement/refactor-db-paths/assets/screenshots/deposit-page.png)                                |
| **Withdrawal Page**                | ![Withdrawal Page](https://github.com/wesamAlsaleh/BankingWithJava/blob/improvement/refactor-db-paths/assets/screenshots/withdraw-page.png)                            |
| **Select Debit Card Page**         | ![Select Debit Card Page](https://github.com/wesamAlsaleh/BankingWithJava/blob/improvement/refactor-db-paths/assets/screenshots/select-debit-card-to-use-page.png)     |
| **Transfer Using Debit Card Page** | ![Transfer Using Debit Card Page](https://github.com/wesamAlsaleh/BankingWithJava/blob/feature/add-banker-users/assets/screenshots/transfer-using-debit-card-page.png) |
| **User Transaction History**       | ![Transaction History](https://github.com/wesamAlsaleh/BankingWithJava/blob/improvement/refactor-db-paths/assets/screenshots/user-transactions-history-page.png)       |

### 3. Banker Views

| Description            | Screenshot                                                                                                                                          |
|:-----------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------|
| **Banker Homepage**    | ![Banker Homepage](https://github.com/wesamAlsaleh/BankingWithJava/blob/improvement/refactor-db-paths/assets/screenshots/banker-page.png)           | |
| **See All Currencies** | ![See All Accounts](https://github.com/wesamAlsaleh/BankingWithJava/blob/improvement/refactor-db-paths/assets/screenshots/currencies-list-page.png) |
---


## 🤝 Contribution

This project was developed solely as part of the GA Java Bootcamp curriculum.

## 👨‍💻 Author

**Wesam Alsaleh**

* [GitHub Profile Link](https://github.com/wesamAlsaleh)

## 📜 License

This project is open-source and available under the MIT License.