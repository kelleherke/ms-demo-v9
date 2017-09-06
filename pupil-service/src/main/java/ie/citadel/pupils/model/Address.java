package ie.citadel.pupils.model;

public class Address {
    
	String eircode;

    String address1;

    String address2;
    
    String address3;
    
    String address4;


    public String getEircode() {
        return eircode;
    }


    public void setEircode(String id) {
        this.eircode = id;
    }
    
    public String getAddress1() {
		return address1;
	}


	public void setAddress1(String address1) {
		this.address1 = address1;
	}


	public String getAddress2() {
		return address2;
	}


	public void setAddress2(String address2) {
		this.address2 = address2;
	}


	public String getAddress3() {
		return address3;
	}


	public void setAddress3(String address3) {
		this.address3 = address3;
	}


	public String getAddress4() {
		return address4;
	}


	public void setAddress4(String address4) {
		this.address4 = address4;
	}


	public Address withEircode(String eircode){
        this.eircode = eircode;
        return this;
    }
	
	public Address withAddress1(String address1){
        this.address1 = address1;
        return this;
    }
	
	public Address withAddress2(String address2){
        this.address2 = address2;
        return this;
    }

	public Address withAddress3(String address3){
        this.address3 = address3;
        return this;
    }

	public Address withAddress4(String address4){
        this.address4 = address4;
        return this;
    }
	
	@Override
	public String toString(){
		return eircode + "," + address1 + "," + address2 + "," + address3 + "," + address4 + "," ;
	}


}