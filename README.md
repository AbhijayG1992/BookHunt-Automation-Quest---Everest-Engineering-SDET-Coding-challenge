# SDET Book Retailer Assignment

This project is a Java-based solution for the SDET assignment. It uses Selenium to find the cheapest online retailer for a given list of books.

## Setup

1.  **Prerequisites:**
    *   Java 11 or higher
    *   Maven
    *   Google Chrome
    *   ChromeDriver (must be in your system's PATH)

2.  **Installation:**
    *   Clone the repository.
    *   Run `mvn install` to download dependencies.

## Execution

1.  **Run tests:**
    *   Run `mvn test` to execute the tests.

2.  **Results:**
    *   The results will be saved in `output.json`.
    *   A detailed report will be generated in the `target` directory.

## Results Format

The `output.json` file will contain the following information:

```json
{
  "cheapestRetailer": "Amazon",
  "totalPrice": 1234.56,
  "books": [
    {
      "book": {
        "name": "The Lord of the Rings",
        "author": "J.R.R. Tolkien",
        "publisher": "George Allen & Unwin",
        "quantity": 1
      },
      "retailer": {
        "name": "Amazon",
        "url": "https://www.amazon.in"
      },
      "price": 500.0,
      "available": true
    }
  ]
}
```
