# Web-Monitor: Application for Monitoring New Tweets on Twitter

Web-Monitor is a microservices-based application designed for monitoring the appearance of new tweets from specified users on the Twitter platform. The application provides high-speed monitoring capabilities and supports various modes of operation. Captured tweets can be displayed either via Discord Webhooks or through a full-fledged frontend with a user interface (UI).

## Technologies Used
- Java 11
- Spring Boot
- PostgreSQL
- MongoDB
- Kafka

## Microservices Overview
The application consists of multiple microservices, each serving a specific purpose:

1. **auth-service**: Implements user authentication logic for the application.
2. **details-service**: Allows customization of application settings for specific user groups (Discord Guilds).
3. **dictionary-service**: Customizes monitoring settings for individual users within a specific Discord Guild.
4. **discord-broadcast-service**: Sends captured tweets and system messages to designated Discord Webhooks.
5. **web-broadcast-service**: Sends captured tweets and system messages to the frontend.
6. **twitter-monitor**: The main component responsible for the tweet monitoring logic.

## Prerequisites
To work with this application, you will need the following:
- Multiple Twitter developer accounts.
- MongoDB instance to store and manage Twitter developer account credentials.
- Access to Discord Webhooks for broadcasting tweets and system messages.
- Familiarity with the Java programming language and Spring Boot framework.

## Getting Started
To get started with the Web-Monitor application, follow these steps:

1. Clone the GitHub repository: `git clone https://github.com/sixblades32/web-monitor.git`.
2. Install the necessary dependencies as specified in the project's documentation.
3. Configure the Twitter developer accounts and their corresponding credentials in MongoDB.
4. Configure the Discord Webhooks for broadcasting purposes.
5. Build and run each microservice using the provided build and deployment instructions.
6. Access the frontend UI or Discord channels to start monitoring new tweets from the specified users.

## Contributing
Contributions to Web-Monitor are welcome! If you want to contribute, please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Make the necessary code changes and write appropriate tests.
4. Commit your changes and push the branch to your forked repository.
5. Submit a pull request to the main repository, describing your changes in detail.

## License
This project is licensed under the [MIT License](https://opensource.org/licenses/MIT). Feel free to modify and distribute it as per the license terms.

Thank you for choosing Web-Monitor!
