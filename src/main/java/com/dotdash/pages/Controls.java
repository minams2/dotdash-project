package com.dotdash.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.dotdash.testbase.TestBase;
//This class will have methods to addTask, add Category, removeTask,completeTask. It uses @pageFaactory to initialize webelements.
//addTask will read data from excel file and before adding the task will call validateTask  to check if the task exists or not, if it already exists it will not the add the task.
//addCategory enters category, select color and calls validate color to see if the color exists or not in the dropdown, if the color exist it will not add the category otherwise it will add.
//remove task will get the task that needs to removed and once remove will call validateTask to see if the task exists or  not
//completeTask will get the task that needs to be completed once completed it will check if the strike is displayed or not
//updateTask clicks on tasknumber and enters value and check if the task already exists or not if the same task exists it updates other values and if the task does not exist will
//update all the values.

public class Controls extends TestBase {
	
	
@FindBy(name="data")
WebElement taskInput;
	
@FindBy(xpath="//input[@name='submit' and @value='Add']")
WebElement addBtn;

static Logger log = Logger.getLogger(Logger.class.getName());

public Controls()  {
	PageFactory.initElements(driver, this);
}
static int j=0;
//addTask will read data from dotdash.xlsx and before entering the data will call validateTask if the task exists or not. If the task exists it will not the add the task

public boolean addTask(String task, String catText, String dayValue, String monthValue, String yearValue) {
	
	boolean flag ;

	if(validateTask(task)==false){
	driver.findElement(By.xpath("//input[@name='data']")).clear();
	driver.findElement(By.xpath("//input[@name='data']")).sendKeys(task);
	
	Select category = new Select(driver.findElement(By.name("category")));
	category.selectByVisibleText(catText);
	
	Select day = new Select(driver.findElement(By.name("due_day")));
	int i = dayValue.indexOf(".");
	day.selectByValue(dayValue.substring(0, i));
	Select month = new Select(driver.findElement(By.name("due_month")));
	int j = monthValue.indexOf(".");
	month.selectByValue(monthValue.substring(0, j));
	Select year = new Select(driver.findElement(By.name("due_year")));
	int k = yearValue.indexOf(".");
	year.selectByValue(yearValue.substring(0, k));
    driver.findElement(By.xpath("//input[@name='submit' and @value='Add']")).click();
    //flag = true;
	
    if(validateTask(task))
    	log.info("********************************************");
    	log.info("Task does not exist and is added");
    	log.info("********************************************");
    	System.out.println("Task does not exist and is added");
     flag = true;
     return flag;
	   } 
	else
		
	log.info("********************************************");
	log.info("SameTask Exists and is not added");
	log.info("********************************************");
	//driver.navigate().back();
	System.out.println("SameTask Exists and is not added");
	 flag = false;
	return flag;
	}

//This is will loop through li tag and check if the task exists or not, if it exists it will return true or false
public  static boolean  validateTask(String task) {
	
	boolean istaskAval = false;
	List<WebElement> list =  driver.findElements(By.tagName("li"));
	for(int i= 0; i<list.size(); i++) {
		String availTask=list.get(i).getText();
		int x= availTask.indexOf(" (");
		 availTask=list.get(i).getText().substring(3, x);
		//System.out.println(availTask);
		if(availTask.equals(task)) {
			 istaskAval =true;
		     // System.out.println(i+"task exists");
			break;
		}
	}
	return istaskAval;
}
//Before adding category will call validate color to see if the color already exists, if the same color exists it will not add the category otherwise it will add the category
public static void addCategory(String category) throws InterruptedException {
	int counter,i=1;
	boolean catCheck=false;

	Select color = new Select(driver.findElement(By.name("colour")));
    List<WebElement> list = color.getOptions();
	counter = list.size()-1;
	
	//System.out.println("counter val"+counter);
	//--counter>1
	while(i<=counter) {
		System.out.println((i<=counter));
		//--if(validateColor(counter,category)==true)
		if(validateColor(i,category)==true) {
			//--counter = counter-1;
			//System.out.println("counter----- val"+counter);
			i=i+1;
		}
		else {
			System.out.println((i<=counter));
		driver.findElement(By.name("categorydata")).clear();
		driver.findElement(By.name("categorydata")).sendKeys(category);
		//--color.selectByIndex(counter);
		color.selectByIndex(i);
		driver.findElement(By.xpath("//input[@type='submit' and @value='Add category']")).click();
		driver.findElement(By.name("categorydata")).clear();
		break;
	}
	}	
	
			Select catDrop = new Select(driver.findElement(By.name("category")));
			List<WebElement> catList = catDrop.getOptions();
			for(int j=0;j<catList.size();j++) {
				String availCat = catList.get(j).getText();
				//System.out.println("avaliable Category"+availCat);
				if(availCat.equals(category)) { 
					//catCheck=true;
					System.out.println("Same Category exists already");
					break;
					//return true;
				}
				else
				{
				//catCheck=false;	
				}
				driver.findElement(By.name("categorydata")).clear();
				driver.findElement(By.name("categorydata")).sendKeys(category);
				//color.selectByIndex(1);
				driver.findElement(By.xpath("//input[@type='submit' and @value='Add category']")).click();
				driver.findElement(By.name("categorydata")).clear();
				System.out.println("Category does not exist and is added");
				
			
	          }
		}	
		


//This will enter category and color and validate if the error page is shown or not, if the error page is shown it will navigate back &return true otherwise return false
	public static boolean  validateColor(int index, String category) {
			
		driver.findElement(By.name("categorydata")).clear();
		driver.findElement(By.name("categorydata")).sendKeys(category);
		Select color = new Select(driver.findElement(By.name("colour")));
		color.selectByIndex(index);
		driver.findElement(By.xpath("//input[@type='submit' and @value='Add category']")).click();
		String errPg= driver.getCurrentUrl();
		if(errPg.equals("http://localhost/dotdash-project/todo.php"))
				{
			
				driver.navigate().back();
				return true;
				
				}
		else
			 return false;
		
		
	}
	//remove task will get the task that needs to removed and once remove will call validateTask to see if the task exists or  not	
	public static boolean removeTask(String taskname) {
		String taskName = "\'+taskname+\'";
	
		driver.findElement(By.xpath("//*[contains(text(),taskName)]//input[@type='checkbox']")).click();
		driver.findElement(By.xpath("//input[@type='submit' and @value='Remove']")).click();
		//js.executeScript("document.getElementsByTagName(\"li\")[7].children[1].click();");
		boolean flag = validateTask(taskname);
		return flag;
	
	}
	//completeTask will get the task that needs to be completed once completed it will check if the strike is displayed or not
	public static boolean completeTask(String taskname) {
		String newtext="\'+taskname+\'";
		
		driver.findElement(By.xpath("//*[contains(text(),newtext)]//input[@type='checkbox']")).click();
		driver.findElement(By.xpath("//input[@type='submit' and @value='Complete']")).click();
		//js.executeScript("document.getElementsByTagName(\"li\")[7].children[1].click();");
		WebElement elem =driver.findElement(By.tagName("strike"));
		if(elem.equals("strike"))
		return true;
		else
		return false;	
	}
	//updateTask clicks on tasknumber and enters value and check if the task already exists or not if the same task exists it updates other values and if the task does not exist will
	//update all the values.	
	public boolean updateTask(String taskNumber,String task, String catText, int dayind, int monthind, int yearind) {
		boolean flag;
		String task1="\'+taskname+\'";
		driver.findElement(By.xpath("//a[contains(text(),task1)]")).click();
				
		driver.findElement(By.xpath("//input[@name='data']")).clear();
		driver.findElement(By.xpath("//input[@name='data']")).sendKeys(task);
		
		Select category = new Select(driver.findElement(By.name("category")));
		category.selectByVisibleText(catText);
		
		Select day = new Select(driver.findElement(By.name("due_day")));
		//int i = dayValue.indexOf(".");
		day.selectByIndex(dayind);
		Select month = new Select(driver.findElement(By.name("due_month")));
		//int j = monthValue.indexOf(".");
		//month.selectByValue(monthValue.substring(0, j));
		month.selectByIndex(monthind);
		Select year = new Select(driver.findElement(By.name("due_year")));
		//int k = yearValue.indexOf(".");
		//year.selectByValue(yearValue.substring(0, k));
		year.selectByIndex(yearind);
	    driver.findElement(By.xpath("//input[@name='submit' and @value='Update']")).click();
	    String pageUrl = driver.getCurrentUrl();
	    if(pageUrl.equals("http://localhost/dotdash-project/edit.php"))
	    {
	    	driver.navigate().back();
	       System.out.println("Task exists already and is not updated");
	       driver.navigate().back();
	       if(validateTask(task))
	       {
	    	   flag=false;
	    	   if((!(pageUrl.equals("http://localhost/dotdash-project/edit.php")))) {
	    		   System.out.println((!(pageUrl.equals("http://localhost/dotdash-project/edit.php"))));
	    		   System.out.println("Task exists already and but udpated with other values");
	    		   flag=true;
	    		   }
	           return flag;
	       }
	     
	    }
	    else
	    	System.out.println("Task is updated successfully");
	        flag = validateTask(task);
	        //System.out.println("Taskupdated flag"+flag);
	        return flag;
		}
}
