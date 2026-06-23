import { test, expect } from '@playwright/test';

test('should create an account and login with that account', async ({ page }) => {
  const timestamp = Date.now();

  const testUser = {
    name: `E2E User ${timestamp}`,
    email: `e2e-${timestamp}@test.com`,
    password: 'Test123!',
  };

  await page.goto('/register');

  await page.getByLabel(/username/i).fill(testUser.name);
  await page.getByLabel(/email/i).fill(testUser.email);
  await page.getByRole('textbox', { name: 'Password', exact: true }).fill(testUser.password);
  await page.getByRole('textbox', { name: 'Confirm password', exact: true }).fill(testUser.password);

  await page.getByRole('button', { name: /Register/i }).click();

await expect(page).toHaveURL(/\/login/, {
  timeout: 10000,
});

  await page.goto('/login');

  await page.getByLabel(/email/i).fill(testUser.email);
  await page.getByLabel(/password/i).fill(testUser.password);

  await page.getByRole('button', { name: /login|sign in/i }).click();


  await expect.poll(async () => {
    return await page.evaluate(() => localStorage.getItem('accessToken'));
  }).not.toBeNull();

  const token = await page.evaluate(() => localStorage.getItem('accessToken'));
  expect(token.length).toBeGreaterThan(10);
});

test('should create an bet with a test account', async ({ page }) => {

  const timestamp = Date.now();
  const testUser = {
    email: 'test@gmail.com',
    password: 'test',
  };
  const testBet = {
    title:`E2E test`,
    description: `test description for E2E`,
    betPrice: `10.00`,
    betEndsAt: '2030-12-31T23:59',
    optionOne: 'Yes',
    optionTwo: 'No',
  };

  await page.goto('/login');
  await page.getByLabel(/email/i).fill(testUser.email);
  await page.getByRole('textbox', { name: 'Password', exact: true }).fill(testUser.password);

  await page.getByRole('button', { name: /login/i }).click();

  await expect.poll(async () => {
    return await page.evaluate(() => localStorage.getItem('accessToken'));
  }).not.toBeNull();

  await page.goto('/create-bet');

  await page.getByTestId("title").fill(testBet.title);
  await page.getByTestId("description").fill(testBet.description);
  await page.getByTestId("bet-price").fill(testBet.betPrice);
  await page.getByTestId("bet-ends-at").fill(testBet.betEndsAt);

  await page.getByTestId("option-0").fill(testBet.optionOne);
  await page.getByTestId("option-1").fill(testBet.optionTwo);
  await page.getByTestId("option-radio-0").check();

  await page.getByTestId("submit-create-bet").click();

  const createBetResponsePromise = page.waitForResponse(response =>
    response.url().includes('/Bets/CreateBet') && response.status() === 200
  );

  await page.getByRole('button', { name: /create|continue|submit/i }).click();

  const createBetResponse = await createBetResponsePromise;
  const createBetBody = await createBetResponse.json();

  expect(createBetBody.title).toBe(testBet.title);
  expect(createBetBody.checkoutUrl).toBeTruthy();

});