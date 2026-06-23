import { test, expect } from '@playwright/test';

test.beforeEach(async ({ page }) => {
  await page.route('**/Bets/GetBetsPerPage**', async route => {
    await route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: JSON.stringify({
        bets: [
          {
            id: 1,
            title: 'Will PSV win the league?',
            description: 'A test bet for E2E testing',
            betPrice: 5,
            betEndsAt: '2026-12-31T23:59:59',
            creatorName: 'qb',
            status: 'OPEN',
            betOptions: [
              { id: 1, optionText: 'Yes' },
              { id: 2, optionText: 'No' },
            ],
          },
        ],
      }),
    });
  });
});

test('should show bets on the landing page', async ({ page }) => {
  await page.goto('/');

  await expect(page.getByText('Will PSV win the league?')).toBeVisible();
});