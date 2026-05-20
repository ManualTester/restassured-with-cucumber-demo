public void click(By locator) {

    int retries = 3;

    for (int i = 1; i <= retries; i++) {

        try {

            WebElement element = wait.until(
                ExpectedConditions.elementToBeClickable(locator)
            );

            scrollIntoView(element);

            highlight(element);

            long start = System.currentTimeMillis();

            try {

                element.click();

            } catch (ElementClickInterceptedException e) {

                jsClick(element);

                logger.warn(
                    "JS click fallback used for {}",
                    locator
                );
            }

            long duration =
                System.currentTimeMillis() - start;

            logger.info(
                "Clicked {} in {} ms",
                locator,
                duration
            );

            return;

        } catch (StaleElementReferenceException e) {

            logger.warn(
                "Retry {} for stale element {}",
                i,
                locator
            );

        } catch (Exception e) {

            captureScreenshot();

            logger.error(
                "Failed clicking {}",
                locator,
                e
            );

            throw new FrameworkException(
                "Unable to click element: " + locator,
                e
            );
        }
    }

    throw new FrameworkException(
        "Max retries exceeded for: " + locator
    );
}