# VAgent - AI Agent Application

A full-stack React and Node.js application with automated testing using Selenium.

## 🚀 Features

- **Frontend**: React.js with Tailwind CSS
- **Backend**: Node.js with Express
- **Testing**: Java Selenium WebDriver with TestNG
- **CI/CD**: GitHub Actions
- **Hosting**: Render.com

## 📋 Prerequisites

- Node.js 18+
- Java 11+
- Maven 3.6+
- Chrome browser (for local testing)

## 🔧 Setup

### 1. Clone the repository

```bash
git clone https://github.com/vdnvarma/VAgentBeta.git
cd VAgentBeta
```

### 2. Environment Configuration

```bash
# Copy environment template
cp .env.example .env

# Edit .env with your actual values
# Never commit .env to version control
```

### 3. Install Dependencies

#### Frontend
```bash
cd frontend
npm install
npm run dev
```

#### Backend
```bash
cd backend
npm install
npm start
```

### 4. Run Selenium Tests

```bash
cd selenium-tests
mvn clean test
```

## 🧪 Testing

### Local Testing
```bash
# Run all tests
cd selenium-tests
mvn test

# Run specific test class
mvn test -Dtest=AuthenticationTest

# Run with custom URL
mvn test -DbaseUrl=https://your-app.com
```

### CI/CD Testing
Tests automatically run on:
- Push to main/develop branches
- Pull requests
- Daily schedule (9 AM UTC)

## 🔒 Security

### Environment Variables
All sensitive data is stored in environment variables:
- `BASE_URL`: Application URL
- `TEST_EMAIL`: Test user email
- `TEST_PASSWORD`: Test user password

### GitHub Secrets
Configure these secrets in your GitHub repository:
1. Go to Settings → Secrets and variables → Actions
2. Add the following secrets:
   - `BASE_URL`: Your production URL
   - `TEST_EMAIL`: Test account email
   - `TEST_PASSWORD`: Test account password
   - `RENDER_DEPLOY_HOOK`: Render deployment webhook (optional)

## 📊 Test Reports

Test reports are automatically generated and uploaded as artifacts:
- **Location**: GitHub Actions → Artifacts
- **Format**: HTML reports with screenshots
- **Retention**: 30 days

## 🔧 Configuration

### Selenium Tests Configuration
- **Timeouts**: Optimized for Render.com free tier
- **Browsers**: Chrome (headless in CI)
- **Retries**: 3 attempts for network resilience

### Test Structure
```
selenium-tests/
├── src/
│   ├── main/java/com/vagent/
│   │   ├── config/         # Configuration management
│   │   ├── driver/         # WebDriver factory
│   │   ├── pages/          # Page Object Model
│   │   └── utils/          # Utilities
│   └── test/java/com/vagent/tests/
│       ├── AuthenticationTest.java
│       ├── ConnectivityTest.java
│       └── ProjectManagementTest.java
```

## 🚀 Deployment

### Automatic Deployment
- **Trigger**: Push to main branch
- **Platform**: Render.com
- **Process**: Build → Deploy → Test

### Manual Deployment
```bash
# Trigger deployment via webhook
curl -X POST "$RENDER_DEPLOY_HOOK"
```

## 📈 Monitoring

### Test Execution
- **Frequency**: On every push + daily
- **Browsers**: Chrome (extensible to Firefox, Edge)
- **Environments**: Production monitoring

### Failure Notifications
- **GitHub**: Pull request checks
- **Artifacts**: Detailed reports and screenshots
- **Logs**: Complete execution logs

## 🛠️ Development

### Adding New Tests
1. Create new test class in `selenium-tests/src/test/java/com/vagent/tests/`
2. Extend `BaseTest` class
3. Use Page Object Model pattern
4. Add appropriate assertions

### Page Objects
1. Create page class in `selenium-tests/src/main/java/com/vagent/pages/`
2. Extend `BasePage` class
3. Define page elements with `@FindBy`
4. Implement page methods

## 🤝 Contributing

1. Fork the repository
2. Create feature branch
3. Commit changes
4. Push to branch
5. Create Pull Request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Support

For issues and questions:
- Create GitHub issue
- Check test reports in Actions
- Review configuration documentation
