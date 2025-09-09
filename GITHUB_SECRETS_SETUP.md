# 🔒 GitHub Secrets Setup Guide

## Required Secrets for CI/CD Pipeline

### 1. Go to Your Repository Settings
1. Navigate to https://github.com/vdnvarma/VAgentBeta
2. Click on **Settings** tab
3. Go to **Secrets and variables** → **Actions**

### 2. Add Repository Secrets

Click **New repository secret** and add these one by one:

#### Required Secrets:

| Secret Name | Description | Example Value |
|-------------|-------------|---------------|
| `BASE_URL` | Your production application URL | `https://vagentbeta.onrender.com` |
| `TEST_EMAIL` | Test user email for automation | `test@example.com` |
| `TEST_PASSWORD` | Test user password | `password123` |

#### Optional Secrets:

| Secret Name | Description | Example Value |
|-------------|-------------|---------------|
| `RENDER_DEPLOY_HOOK` | Render deployment webhook URL | `https://api.render.com/deploy/srv-xxx?key=xxx` |

### 3. How to Add Each Secret

For each secret:
1. Click **New repository secret**
2. Enter the **Name** (exactly as shown above)
3. Enter the **Value** (your actual sensitive data)
4. Click **Add secret**

### 4. Render Deploy Hook (Optional)

To get your Render deploy hook:
1. Go to your Render dashboard
2. Select your service
3. Go to **Settings**
4. Scroll to **Deploy Hook**
5. Copy the URL and add it as `RENDER_DEPLOY_HOOK` secret

## 🔐 Security Best Practices

### ✅ What We've Secured:
- ✅ All URLs moved to environment variables
- ✅ Test credentials stored in GitHub Secrets
- ✅ No sensitive data in source code
- ✅ Comprehensive `.gitignore` file
- ✅ Environment template (`.env.example`)

### ✅ Additional Security Features:
- ✅ Automated dependency vulnerability scanning
- ✅ Branch protection (recommended)
- ✅ Required status checks
- ✅ Security policy documentation

## 🚀 After Setup

Once you add the secrets:
1. **Automatic Testing**: Tests will run on every push
2. **Daily Monitoring**: Tests run daily at 9 AM UTC
3. **Pull Request Checks**: Tests run on all PRs
4. **Deployment Integration**: Automatic deployment to Render

## 📊 Accessing Test Results

### View Test Reports:
1. Go to **Actions** tab in your repository
2. Click on any workflow run
3. Download **selenium-test-reports** artifacts
4. View detailed HTML reports and screenshots

### Monitor Test Status:
- ✅ Green checkmark = All tests passed
- ❌ Red X = Tests failed (check logs)
- 🟡 Yellow dot = Tests running

## 🛠️ Troubleshooting

### Common Issues:

1. **Tests failing due to URL**:
   - Check `BASE_URL` secret is correct
   - Ensure application is accessible

2. **Authentication tests failing**:
   - Verify `TEST_EMAIL` and `TEST_PASSWORD` secrets
   - Check if test user exists in your system

3. **Deployment hook not working**:
   - Verify `RENDER_DEPLOY_HOOK` URL is correct
   - Check Render service settings

## 📞 Support

If you encounter issues:
1. Check the Actions logs for detailed error messages
2. Verify all secrets are correctly configured
3. Ensure your application is accessible at the BASE_URL
4. Review the test reports for specific failure details
