package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@Autowired
	EncryptionService encryptionService;

	@Autowired
	CredentialService credentialsService;

	@Autowired
	UserService userService;

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.firefoxdriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new FirefoxDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

	@Test
	public void unauthorizedTest() {
		driver.get("http://localhost:" + this.port + "/login");
		assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());

		driver.get("http://localhost:" + this.port + "/signup");
		assertEquals("http://localhost:" + this.port + "/signup", driver.getCurrentUrl());

		driver.get("http://localhost:" + this.port + "/home");
		assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());

		driver.get("http://localhost:" + this.port + "/upload/file");
		assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());

		driver.get("http://localhost:" + this.port + "/note");
		assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());

		driver.get("http://localhost:" + this.port + "/credential");
		assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	@Test
	public void authorizedTest() {
		doMockSignUp("authorize", "Test", "authorizeTest", "000");
		doLogIn("authorizeTest", "000");
		assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
		WebDriverWait wait = new WebDriverWait(driver, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-button"))).click();
		assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
		driver.get("http://localhost:" + this.port + "/home");
		assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	@Test
	public void noteCreationTest() {
		String title = "noteTitle";
		String description = "noteDescription";

		doMockSignUp("noteCreation", "Test", "noteCreation", "000");
		doLogIn("noteCreation", "000");

		WebDriverWait wait = new WebDriverWait(driver, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement noteNavBar = driver.findElement(By.id("nav-notes-tab"));
		noteNavBar.click();
		createNote(title, description);

	}

	@Test
	public void noteDeleteTest() {
		String title = "noteTitle";
		String description = "noteDescription";

		doMockSignUp("noteDelete", "Test", "noteDelete", "111");
		doLogIn("noteDelete", "111");

		WebDriverWait wait = new WebDriverWait(driver, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement noteNavBar = driver.findElement(By.id("nav-notes-tab"));
		noteNavBar.click();
		createNote(title, description);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-button"))).click();
		redirectToHome(wait);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab"))).click();
		assertThrows(NoSuchElementException.class, () -> driver.findElement(By.id("noteTitleRecord")));
		assertThrows(NoSuchElementException.class, () -> driver.findElement(By.id("noteDescriptionRecord")));
	}

	@Test
	public void noteEditTest() {
		String title = "noteTitle";
		String description = "noteDescription";

		doMockSignUp("noteEdit", "Test", "noteEdit", "111");
		doLogIn("noteEdit", "111");

		WebDriverWait wait = new WebDriverWait(driver, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement noteNavBar = driver.findElement(By.id("nav-notes-tab"));
		noteNavBar.click();
		createNote(title, description);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-button"))).click();

		String newTitle = "newTitle";
		String newDescription = "newDescription";
		typeInNoteDetails(newTitle, newDescription);
		redirectToHome(wait);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteTitleRecord")));
		WebElement noteTitle = driver.findElement(By.id("noteTitleRecord"));
		WebElement noteDescription = driver.findElement(By.id("noteDescriptionRecord"));

		Assertions.assertTrue(noteTitle.getText().contains(newTitle));
		Assertions.assertTrue(noteDescription.getText().contains(newDescription));

	}

	@Test
	public void credentialCreateTest() throws Exception{
		String url = "www.credential.test";
		String username = "test";
		String password = "pwd";

		doMockSignUp("credentialCreation", "Test", "credentialCreation", "000");
		doLogIn("credentialCreation", "000");

		WebDriverWait wait = new WebDriverWait(driver, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement credentialNavBar = driver.findElement(By.id("nav-credentials-tab"));
		credentialNavBar.click();
		createCredential(url, username, password);
	}

	@Test
	public void credentialEditTest() throws Exception{
		String url = "www.credential.test";
		String username = "test";
		String password = "pwd";

		doMockSignUp("credentialCreation", "Test", "credentialCreation", "000");
		doLogIn("credentialCreation", "000");

		WebDriverWait wait = new WebDriverWait(driver, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement credentialNavBar = driver.findElement(By.id("nav-credentials-tab"));
		credentialNavBar.click();
		createCredential(url, username, password);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-credential"))).click();

		String newUrl = "www.credential.test1";
		String newUsername = "test1";
		String newPassword = "pwd1";
		typeInCredentialDetails(newUrl, newUsername, newPassword);
		redirectToHome(wait);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialUrl")));
		WebElement credentialUrl = driver.findElement(By.id("credentialUrl"));
		WebElement credentialUsername = driver.findElement(By.id("credentialUsername"));
		WebElement credentialPassword = driver.findElement(By.id("credentialPwd"));
		String decryptedpwd = getDecryptedText(credentialPassword.getText());

		Assertions.assertTrue(credentialUrl.getText().contains(newUrl));
		Assertions.assertTrue(credentialUsername.getText().contains(newUsername));
		Assertions.assertTrue(decryptedpwd.contains(newPassword));
	}

	@Test
	public void credentialDeleteTest() throws Exception{
		String url = "www.credential.test";
		String username = "test";
		String password = "pwd";

		doMockSignUp("credentialCreation", "Test", "credentialCreation", "000");
		doLogIn("credentialCreation", "000");

		WebDriverWait wait = new WebDriverWait(driver, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement credentialNavBar = driver.findElement(By.id("nav-credentials-tab"));
		credentialNavBar.click();
		createCredential(url, username, password);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-credential"))).click();
		redirectToHome(wait);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab"))).click();
		assertThrows(NoSuchElementException.class, () -> driver.findElement(By.id("credentialUrl")));
		assertThrows(NoSuchElementException.class, () -> driver.findElement(By.id("credentialUsername")));
		assertThrows(NoSuchElementException.class, () -> driver.findElement(By.id("credentialPwd")));
	}

	private void createCredential(String url, String username, String password) throws Exception {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-creation-button")));
		WebElement createCredentialButton = driver.findElement(By.id("credential-creation-button"));
		createCredentialButton.click();

		typeInCredentialDetails(url, username, password);
		redirectToHome(webDriverWait);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab"))).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialUrl")));
		WebElement credentialUrl = driver.findElement(By.id("credentialUrl"));
		WebElement credentialUsername = driver.findElement(By.id("credentialUsername"));
		WebElement credentialPassword = driver.findElement(By.id("credentialPwd"));
		String decryptedpwd = getDecryptedText(credentialPassword.getText());

		Assertions.assertTrue(credentialUrl.getText().contains(url));
		Assertions.assertTrue(credentialUsername.getText().contains(username));
		Assertions.assertTrue(decryptedpwd.contains(password));

	}

	private String getDecryptedText(String encryptedPassword) throws Exception {
		User user = userService.getUser("credentialCreation");
		Credential credential = credentialsService.getCredentialsForUser(user.getUserId()).get(0);
		return encryptionService.decryptValue(encryptedPassword, credential.getKey());
	}

	private void typeInCredentialDetails(String url, String username, String password) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement credentialUrl = driver.findElement(By.id("credential-url"));
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		WebElement credentialPwd = driver.findElement(By.id("credential-password"));

		credentialUrl.click();
		credentialUrl.sendKeys(url);

		credentialUsername.click();
		credentialUsername.sendKeys(username);

		credentialPwd.click();
		credentialPwd.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-submit")));
		WebElement credentialSubmit = driver.findElement(By.id("credential-submit"));
		credentialSubmit.click();
	}

	private void createNote(String title, String description) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-creation-button")));
		WebElement createNoteButton = driver.findElement(By.id("note-creation-button"));
		createNoteButton.click();

		typeInNoteDetails(title, description);
		redirectToHome(webDriverWait);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab"))).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteTitleRecord")));
		WebElement noteTitle = driver.findElement(By.id("noteTitleRecord"));
		WebElement noteDescription = driver.findElement(By.id("noteDescriptionRecord"));

		Assertions.assertTrue(noteTitle.getText().contains(title));
		Assertions.assertTrue(noteDescription.getText().contains(description));
	}

	private void typeInNoteDetails(String title, String description) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		WebElement noteDescription = driver.findElement(By.id("note-description"));

		noteTitle.click();
		noteTitle.sendKeys(title);

		noteDescription.click();
		noteDescription.sendKeys(description);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-submit")));
		WebElement notesubmit = driver.findElement(By.id("note-submit"));
		notesubmit.click();
	}

	private void redirectToHome(WebDriverWait webDriverWait) {
		// Assert that the operation was successfully
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		driver.findElement(By.id("result-to-home")).click();
		Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

	}


}
