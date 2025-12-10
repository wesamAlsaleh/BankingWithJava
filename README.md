# 🏦 BankingWithJava

This repository contains the solution for **Project 1** of the **GA Java Bootcamp** (November 2025). The project is a simulation of a basic banking system implemented in Java, focusing on core object-oriented programming (OOP) principles and file handling for persistence.

## 🌟 Features

* **Account Management:** Create, read, update, and delete bank accounts.
* **Transaction Processing:** Support for `DEPOSIT`, `WITHDRAW` and `TRANSFER` transactions.
* **Overdraft Protection:** Implements specific business rules for handling overdrafts, including fees and account deactivation.
* **Card Management:** Logic to read and validate debit card information.
* **File Persistence:** All account and transaction data are stored in files within a simulated database directory structure.
* **Currency Conversion:** Handles transactions across different currencies with a simulated exchange rate service.

## 🚀 Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

You will need the following software installed on your machine:

* **Java Development Kit (JDK):** Version 17 or higher (Recommended).
* **An IDE:** IntelliJ IDEA, VS Code, or Eclipse.

### Installation

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/wesamAlsaleh/BankingWithJava.git
    ```
2.  **Navigate to the Project Directory:**
    ```bash
    cd BankingWithJava
    ```
3.  **Run the Main Application:**
    Locate the main class (`BankSystem.java`) and run it from your IDE or the command line.

---

## 🏗️ System Design: Entity Relationship Diagram (ERD)

The following diagram illustrates the key entities (tables/data models) in the banking system and the relationships between them.

*(**ACTION REQUIRED:** Replace the URL below with the public link to your `ERD.png` file on GitHub or an image host.)*

![Entity Relationship Diagram (ERD)](<link_to_your_ERD.png_file_here>)

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

* **Overdraft Fee:** A fixed **$35 USD** fee is charged for any overdraft transaction, converted to the account's local currency.
* **Negative Balance Limit:** Withdrawals exceeding the equivalent of **$100 USD** are prohibited if the account's current balance is already negative.
* **Account Deactivation:** An account is automatically **deactivated** after reaching **2 overdrafts**.
* **Reactivation:** The system includes logic to reactivate a deactivated account once the customer resolves the negative balance (by depositing funds to bring the balance to $\ge 0$).

## 🤝 Contribution

This project was developed solely as part of the GA Java Bootcamp curriculum.

## 👨‍💻 Author

**Wesam Alsaleh**
* [GitHub Profile Link](https://github.com/wesamAlsaleh)

## 📜 License

This project is open-source and available under the MIT License.