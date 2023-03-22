
public class Car {
	
	private static int counter = 1;
	
	private String id;
	private String brand;
	private String model;
	private String type;
	private int yearOfManufacture;
	private int seats;
	private String colour;
	private double dailyRent;
	private double dailyInsurance;
	private double serviceFee;
	private double rentDiscount;

	public Car(String id, String brand, String model, String type, int yearOfManufacture, int seats, String colour, double dailyRent,
			double dailyInsurance, double serviceFee, double rentDiscount) {
		super();
		this.id = id;
		this.brand = brand;
		this.model = model;
		this.type = type;
		this.yearOfManufacture = yearOfManufacture;
		this.seats = seats;
		this.colour = colour;
		this.dailyRent = dailyRent;
		this.dailyInsurance = dailyInsurance;
		this.serviceFee = serviceFee;
		this.rentDiscount = rentDiscount;
	}
	
	public String getId() {
		return id;
	}

	public String getBrand() {
		return brand;
	}

	public String getModel() {
		return model;
	}

	public String getType() {
		return type;
	}

	public int getYearOfManufacture() {
		return yearOfManufacture;
	}

	public int getSeats() {
		return seats;
	}

	public String getColour() {
		return colour;
	}

	public double getDailyRent() {
		return dailyRent;
	}

	public double getDailyInsurance() {
		return dailyInsurance;
	}

	public double getServiceFee() {
		return serviceFee;
	}

	public double getRentDiscount() {
		return rentDiscount;
	}
	
	// method to return car details in a string. This just adds the vehicle ID to the toSummary
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(this.getId() + " - ");
		sb.append(this.getBrand() + " ");
		sb.append(this.getModel() + " ");
		sb.append(this.getType() + " with ");
		sb.append(this.getSeats() + " seats");
		
		return sb.toString();
	}

	public String vehicleBookingSummary(int daysDiff) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("%-20s%s\n", "Vehicle ID:", this.id));
		sb.append(String.format("%-20s%s\n", "Brand:", this.brand));
		sb.append(String.format("%-20s%s\n", "Model:", this.model));
		sb.append(String.format("%-20s%s\n", "Vehicle type", this.type));
		sb.append(String.format("%-20s%d\n", "Year of Manufacture", this.yearOfManufacture));
		sb.append(String.format("%-20s%d\n", "No. of seats:", this.seats));
		sb.append(String.format("%-20s%s\n", "Colour:", this.colour));
		sb.append(String.format("%-20s$%.2f ($%.2f * %d days)\n", "Rental:", this.dailyRent*daysDiff, this.dailyRent, daysDiff));
		sb.append(String.format("%-20s$%.2f %s\n", "Discounted price:", calculateDiscount(daysDiff), generateDiscountMsg(daysDiff)));
		sb.append(String.format("%-20s$%.2f ($%.2f * %d days)\n", "Insurance:", this.dailyInsurance*daysDiff, this.dailyInsurance, daysDiff));
		sb.append(String.format("%-20s$%.2f\n", "Service fee:", this.serviceFee));
		sb.append(String.format("%-20s$%.2f\n", "Total:", (calculateDiscount(daysDiff)+this.serviceFee +(this.dailyInsurance*daysDiff))));
		
		return sb.toString();
	}

	// calculates discount based on number of rental days
	public double calculateDiscount(int daysDiff) {
		double discount = this.dailyRent*daysDiff;
		if (daysDiff >= 7) {
			discount = this.dailyRent*((100-this.rentDiscount)/100)*daysDiff;
		}
		return discount;
	}
	
	public String generateDiscountMsg(int daysDiff) {
		String message;
		if (daysDiff < 7 || this.rentDiscount < 1) {
			message = "(Discount not applicable)";
		} else {
			message = String.format("($%.2f * %d days)", this.dailyRent*((100-this.rentDiscount)/100), daysDiff);
		}
		return message;
	}	
	
}