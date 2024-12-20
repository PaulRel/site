package customer;

public class Customer {
	public enum Role{
		ADMIN,
		CUSTOMER,
	}
	public enum Civility{
		M,
		Mme,
	}
    private String firstName, lastName, email, phoneNumber, password, address;
    private Role role; private Civility civility;
    private Cart cart;
    private int id;
    
    public Customer(String firstName, String lastName, Civility civility, String email, String phoneNumber, String password, Role role, String address) {
        this.id = 0;
    	this.firstName = firstName;
        this.lastName = lastName;
        this.civility = civility;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        this.address = address;
        this.cart = new Cart(); // Initialize with an empty cart    
    }
    
    public Customer(int id, String firstName, String lastName, Civility civility, String email, String phoneNumber, String password, Role role, String address) {
    	this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.civility = civility;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        this.address = address;
        this.cart = new Cart(); // Initialize with an empty cart       
    }
    
    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

	public Civility getCivility() {
		return civility;
	}

	public void setGender(Civility civility) {
		this.civility = civility;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
