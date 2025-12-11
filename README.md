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
* **An IDE:** Must use IntelliJ IDEA.

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

---

## 🏗️ System Design: Entity Relationship Diagram (ERD)

The following diagram illustrates the key entities (tables/data models) in the banking system and the relationships
between them.

![Entity Relationship Diagram (ERD)](https://github.com/wesamAlsaleh/BankingWithJava/blob/main/assets/ERD.png)

---

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

## 🤝 Contribution

This project was developed solely as part of the GA Java Bootcamp curriculum.

## 📸 Screenshots

Here are some screenshots showcasing the user interface and key functionalities of the banking application:

### 1. Account Creation and Login

| Description              | Screenshot                                                                                                   |
|:-------------------------|:-------------------------------------------------------------------------------------------------------------|
| **Startup / Login Page** | ![Login Page](https://github.com/wesamAlsaleh/BankingWithJava/blob/main/assets/screenshots/startup.png)      |
| **Create Account Page**  | ![Create Account](https://github.com/wesamAlsaleh/BankingWithJava/blob/main/assets/screenshots/register.png) |

### 2. User Dashboards and Transactions

| Description                       | Screenshot                                                                                                                                |
|:----------------------------------|:------------------------------------------------------------------------------------------------------------------------------------------|
| **Customer Homepage**             | ![Customer Homepage](https://github.com/wesamAlsaleh/BankingWithJava/blob/main/assets/screenshots/customer_homepage.png)                  |
| **See All Accounts**              | ![See All Accounts](https://github.com/wesamAlsaleh/BankingWithJava/blob/main/assets/screenshots/see_all_acounts_page.png)                |
| **Deposit Page**                  | ![Deposit Page](https://github.com/wesamAlsaleh/BankingWithJava/blob/main/assets/screenshots/deposit_page.png)                            |
| **Withdrawal Page**               | ![Withdrawal Page](https://github.com/wesamAlsaleh/BankingWithJava/blob/main/assets/screenshots/withdraw_page.png)                        |
| **Select Debit Card Page**        | ![Select Debit Card Page](https://github.com/wesamAlsaleh/BankingWithJava/blob/main/assets/screenshots/use_debit_cards_page.png)          |
| **Deposit using Debit Card Page** | ![Deposit Using Debit Card Page](https://github.com/wesamAlsaleh/BankingWithJava/blob/main/assets/screenshots/deposit_using_dc_page.png)  |
| **Withdrawal Page**               | ![Withdrawal Page](https://github.com/wesamAlsaleh/BankingWithJava/blob/main/assets/screenshots/withdraw_page.png)                        |
| **User Transaction History**      | ![Transaction History](https://github.com/wesamAlsaleh/BankingWithJava/blob/main/assets/screenshots/user_transactions_history_page_2.png) |

### 3. Banker Views

| Description                    | Screenshot                                                                                                                          |
|:-------------------------------|:------------------------------------------------------------------------------------------------------------------------------------|
| **Banker Homepage**            | ![Banker Homepage](https://github.com/wesamAlsaleh/BankingWithJava/blob/main/assets/screenshots/banker_homepage.png)                | |
| **See All Currencies**         | ![See All Accounts](https://github.com/wesamAlsaleh/BankingWithJava/blob/main/assets/screenshots/see_currencies_page.png)           |
| **System Transaction Tracker** | ![System Tracker](https://github.com/wesamAlsaleh/BankingWithJava/blob/main/assets/screenshots/system_transaction_tracker_page.png) |

---


## 👨‍💻 Author

**Wesam Alsaleh**

* [GitHub Profile Link](https://github.com/wesamAlsaleh)

## 📜 License

This project is open-source and available under the MIT License.