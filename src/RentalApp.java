import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class RentalApp {
	

	private String name;
	
	public RentalApp(String string) {
		this.name = string;
	}
	
	Scanner sc = new Scanner(System.in);
	
	private ArrayList<Car> cars = new ArrayList<Car>();
	
	private Car selectedCar;
	
	private int intSelection;
	
	boolean exit = false;
	
	public void run() {
		
		try {
			addRentalCars();
		} catch (FileNotFoundException e) {
			System.out.println("Unable to access Fleet.csv file.");
		}
		
		do {
			System.out.println();
			System.out.println("Welcome to " + name + "!");
			System.out.println("-".repeat(80));
			System.out.println("> Select from main menu");
			System.out.println("-".repeat(80));
			System.out.println();
			System.out.println("[1] Search by brand");
			System.out.println("[2] Browse by vehicle type");
			System.out.println("[3] Filter by number of passenger");
			System.out.println("[4] Exit");
			System.out.println();
			System.out.print("Selection: ");
			
			intSelection = validateIntSel(4);
			
			switch (intSelection) {
			case (1):
				searchByField("Brand");
				break;
			case (2):
				searchByField("Type");
				break;
			case (3):
				searchBySeats();
				break;
			case (4):
				exit = true;
				System.out.println("Thank you for using " + name + ".");
				System.out.println("Exiting...");
				break;
			}
			
		} while (!exit);
					
	}
	
	// read from file Fleet.csv and add Car objects to cars ArrayList.
	private void addRentalCars() throws FileNotFoundException {
		
		Scanner fileScanner = new Scanner(new File("Fleet.csv"));
		
		String rowData = fileScanner.nextLine();
		
		while (fileScanner.hasNextLine()) {
			
			rowData = fileScanner.nextLine();
			String[] list = rowData.split(",");
			
			String id = list[0];
			String brand = list[1];
			String model = list[2];
			String type = list[3];
			int yearOfManufacture = Integer.parseInt(list[4]);
			int seats = Integer.parseInt(list[5]);
			String color = list[6];
			double dailyRent = Double.parseDouble(list[7]);
			double dailyInsurance = Double.parseDouble(list[8]);
			double serviceFee = Double.parseDouble(list[9]);
			double rentDiscount;
			try {
				rentDiscount = Double.parseDouble(list[10]);
			} catch (NumberFormatException e) {
				rentDiscount = 0;
			}
			
			Car car = new Car(id, brand, model, type, yearOfManufacture, seats, color, dailyRent, dailyInsurance, serviceFee, rentDiscount);
			cars.add(car);
		}
		fileScanner.close();
	}
	
	
	// combined both search by Brand and Type into a single method.
	private void searchByField(String string) {
		
		HashSet<String> selectablesSet = new HashSet<String>();
		ArrayList<String> selectablesArray = new ArrayList<String>();
		
		if (string == "Brand") {
			
			for (Car car: cars) {
				selectablesSet.add(car.getBrand());
			}
			
		} else if (string == "Type") {
			
			for (Car car: cars) {
				selectablesSet.add(car.getType());
			}
		}
		
		sectionTitle(string + " Selection");
		
		int counter = 1;
		
		for (String selection: selectablesSet) {
			System.out.println("[" + counter + "] " + selection );
			selectablesArray.add(selection);
			counter++;
		}
		
		System.out.println();
		System.out.print("Selection: ");
		
		int selArraySize = selectablesArray.size();
		int choice = validateIntSel(selArraySize);
		
		System.out.println();
		System.out.println("Matched " + counter + " cars... ");
		System.out.println();
		
		ArrayList<Car> searchResult = new ArrayList<Car>();	
		
		counter = 1;
		
		for (Car car: cars) {
			
			if (car.getBrand().equals(selectablesArray.get(choice-1)) || car.getType().equals(selectablesArray.get(choice-1))) {

				System.out.println(counter + ") " + car.toString());
				searchResult.add(car);
				counter++;
			}
		}
		
		System.out.println(counter + ") Go to main menu");
		System.out.println();
		System.out.print("Selection: ");
		
		selArraySize = searchResult.size();
		
		choice = validateIntSel(counter);
		
		if (choice != counter) {
			selectedCar = searchResult.get(choice-1);
			selectRentDate(selectedCar);
		} else {
			System.out.println("Returning to Main Menu...");
		}
		
	}
	
	// method to search by passenger/seat requirement
	private void searchBySeats() {
		
		ArrayList<Car> searchResult = new ArrayList<Car>();
		
		sectionTitle("Filter by Seats Selection");
		System.out.print("Please provide the maximum number of passengers (including driver): ");
		
		int maxSeats = 0;
		int counter = 0;
		
		for (Car car: cars) {
			if (car.getSeats() > maxSeats) {
				maxSeats = car.getSeats();
				counter++;
			}
		}
				
		int choice = validateIntSel(maxSeats); 
		
		
		System.out.println();
		System.out.println("Matched " + counter+ " cars... ");
		System.out.println();
		
		counter = 1;
		
		for (Car car: cars) {
			if (car.getSeats() >= choice) {

				searchResult.add(car);
				System.out.println(counter + ") " + car.toString());
				counter++;
			}
		}
		
		System.out.println(counter + ") Go to main menu");
		System.out.println();
		System.out.print("Selection: ");
		
		choice = validateIntSel(counter);
		
		if (choice != counter) {
			selectedCar = searchResult.get(choice-1);
			selectRentDate(selectedCar);
		} else {
			System.out.println("Returning to Main Menu...");
		}
	}
	
	
	
	// method to validate dates and input user data
	private void selectRentDate(Car selectedCar2) {

		sectionTitle("Booking Date Selection");
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		LocalDate startDate = null;
		LocalDate endDate = null;
		
		boolean validDate = false;
		do {
			System.out.print("Please provide pick-up date (dd/mm/yyyy): ");
			String dateStart = sc.nextLine();
			
			try {
				startDate = LocalDate.parse(dateStart, dtf);
				validDate = true;
			} catch (DateTimeParseException e) {
				System.out.println("Incorrect date input. Please check and try again.");
			} catch (NullPointerException e) {
				System.out.println("Incorrect date input. Please check and try again.");
			}
			
		} while(!validDate);
		
		validDate = false;
		
		int daysDiff;
		
		do {
			System.out.print("Please provide return date (dd/mm/yyyy): ");
			String dateEnd = sc.nextLine();
			
			try {
				endDate = LocalDate.parse(dateEnd, dtf);
			} catch (DateTimeParseException e) {
				System.out.println("Incorrect date input. Please check and try again.");
			} catch (NullPointerException e) {
				System.out.println("Incorrect date input. Please check and try again.");
			}
			
			daysDiff = Period.between(startDate, endDate).getDays();
			
			if (daysDiff > 0) {
				validDate = true;
			} else {
				System.out.println("Return date cant be before or on the same day as start-date.");
			}
		} while(!validDate);
			
		sectionTitle("Show Vehicle Details");
		
		System.out.printf("%-20s%s\n", "Vehicle ID:", selectedCar.getId());
		System.out.printf("%-20s%s\n", "Brand:", selectedCar.getBrand());
		System.out.printf("%-20s%s\n", "Model:", selectedCar.getModel());
		System.out.printf("%-20s%s\n", "Vehicle type", selectedCar.getType());
		System.out.printf("%-20s%d\n", "Year of Manufacture", selectedCar.getYearOfManufacture());
		System.out.printf("%-20s%d\n", "No. of seats:", selectedCar.getSeats());
		System.out.printf("%-20s%s\n", "Colour:", selectedCar.getColour());
		System.out.printf("%-20s$%.2f ($%.2f * %d days)\n", "Rental:", selectedCar.getDailyRent()*daysDiff, selectedCar.getDailyRent(), daysDiff);
		System.out.printf("%-20s$%.2f %s\n", "Discounted price:", selectedCar.calculateDiscount(daysDiff), selectedCar.generateDiscountMsg(daysDiff));
		System.out.printf("%-20s$%.2f ($%.2f * %d days)\n", "Insurance:", selectedCar.getDailyInsurance()*daysDiff, selectedCar.getDailyInsurance(), daysDiff);
		System.out.printf("%-20s$%.2f\n", "Service fee:", selectedCar.getServiceFee());
		System.out.printf("%-20s$%.2f\n", "Total:", (selectedCar.calculateDiscount(daysDiff)+selectedCar.getServiceFee() +(selectedCar.getDailyInsurance()*daysDiff)));
		System.out.println();
		System.out.print("Would you like to reserve the vehicle (Y/N)? ");
		
		String choice = validateYorN();
		
		if (choice.equalsIgnoreCase("Y")) {
			
			sectionTitle("Provide Personal Information");
			
			String firstName = getDetails("First Name");
			String surName = getDetails("Last Name");
			String email = getDetails("Email");		
			
			System.out.print("Number of Passengers: ");
			
			int seatsInput = validateIntSel(selectedCar.getSeats());
			
			
			System.out.print("Confirm and pay (Y/N): ");
			String userConfirm = validateYorN();
				
			if (userConfirm.equalsIgnoreCase("Y")) {
				System.out.println();
				System.out.println("Congratulations! Your vehicle is booked. A receipt has been sent to your email.");
				System.out.println("We will soon be in touch before your pick-up date.");
				
				sectionTitle("Booking Details");
				
				String vehicleSum = selectedCar.toString().substring(7);
				
				
				System.out.printf("%-25s%s %s\n","Name:", firstName, surName);
				System.out.printf("%-25s%s\n", "Email:", email);
				System.out.printf("%-25s%s\n", "Your vehicle:", vehicleSum);
				System.out.printf("%-25s%d\n", "Number of passengers:", seatsInput);
				System.out.printf("%-25s%s\n", "Pick-up date:", startDate);
				System.out.printf("%-25s%s\n", "Return date:", endDate);
				System.out.printf("%-25s$%.2f\n", "Total payment:", selectedCar.calculateDiscount(daysDiff));
				System.out.println();
				exit = true;
			} else {
				System.out.println("Returning to menu...");
			}
			
		} else {
			System.out.println("Returning to menu...");
		}
		
	}
		
	

	
	// utility method to validate against blank user input.
	private String getDetails(String string) {
		
		String userInput;
		do {
			System.out.print(string +": ");
			userInput = sc.nextLine();
			if (userInput.isBlank()) {
				System.out.println("Cannot have a blank " + string + ".");
			}
		} while (userInput.isBlank());
		
		return userInput;
	}
	
	// utility method to validate user selection of Y or N.
	private String validateYorN() {
		
		String choice = null;
		
		do {
			choice = sc.nextLine();
			if (!choice.equalsIgnoreCase("Y") && !choice.equalsIgnoreCase("N")) {
				System.out.println("Invalid input. Please input either Y or N.");
				System.out.print("Selection: ");
			}
		}while (!choice.equalsIgnoreCase("Y") && !choice.equalsIgnoreCase("N"));
		
		return choice;
	}


	// utility method for section Title output. Just for reducing code repetition.
	private void sectionTitle(String string) {
		
		System.out.println();
		System.out.println("-".repeat(80));
		System.out.println(string);
		System.out.println("-".repeat(80));
		System.out.println();
		
	}
	
	
	// utility method to validate user input as an integer that corresponds to allowed selection choices
	private int validateIntSel(int choice) {
		
		int input = 0;
		boolean validInput = false;
		
		do {
			try {
				input = Integer.parseInt(sc.nextLine());
				if ( input < 1 || input > choice) {
					System.out.print("Invalid input! (Must be from 1 to " + choice + "): ");
				} else {
					validInput = true;
				}	
			} catch (NumberFormatException e) {
				System.out.print("Invalid input! (Must be a numerical value): ");
			}
		} while (!validInput);
		
		return input;
	}
	
	
	
	
}

