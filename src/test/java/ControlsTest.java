import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.dotdash.pages.Controls;
import com.dotdash.testbase.TestBase;
import com.dotdash.util.TestUtil;

//This class will first initialize the selenium  webdriver in the set up method, have @test to add Category,updateTask, removeTask, CompleteTask, addTask,   which use @DataProvider to read the data from excel file and add the task.
//Note: To run the test make it enabled parameter=true
public class ControlsTest extends TestBase {
	Controls controls = new Controls();
	
		
	@BeforeTest
	public void setUp() {
		intialize();
		
	}


	@Test(priority=1,enabled=true)
		public void addCategoryTest() throws InterruptedException {
		controls.addCategory("test8");
		TestUtil.pageScrollToView(driver.findElement(By.xpath("//input[@type='submit' and @value='Add category']")));
	}
	
	@DataProvider
	public Object[][] getData(){
		Object[][] data = TestUtil.getTestData();
		return data;
	}
	
	@Test(priority=2, dataProvider="getData",enabled=false)
	public void addTaskTest(String Task, String Category, String Day, String Month, String Year) {
		boolean flag = controls.addTask(Task, Category, Day, Month, Year);
		Assert.assertTrue(flag);
		TestUtil.pageScrollToView(driver.findElement(By.xpath("//input[@type='submit' and @value='Add category']")));
		
		
	}
	@Test(priority=3,enabled=false)
	public void updateTaskTest() {
	boolean flag = controls.updateTask("19","task5", "Play",4,5,1);
	Assert.assertTrue(flag);
}
	@Test(priority=4,enabled=false)
	public void completeTaskTest() {
	boolean flag=controls.completeTask("four");
	Assert.assertTrue(flag);		
}
	@Test(priority=5,enabled=false)
	public void removeTaskTest() {
	boolean flag=controls.removeTask("four");
	Assert.assertTrue(flag);		
}
}

