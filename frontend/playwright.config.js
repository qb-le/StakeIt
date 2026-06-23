import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
  testDir: './tests',

  reporter: 'html',

  use: {
    baseURL: 'http://localhost',
    trace: 'on-first-retry',
  },

  webServer: {
    command: 'npm run dev',
    url: 'http://localhost',
    reuseExistingServer: true,
    timeout: 120000,
  },

  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
  ],
});