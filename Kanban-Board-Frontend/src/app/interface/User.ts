export class User {
  userId: string;
  firstName: string;
  lastName: string;
  emailId: string;
  password: string;
  phoneNumber: number;
  profilePic: string; // New field for profile picture URL

  constructor(
    userId: string,
    firstName: string,
    lastName: string,
    emailId: string,
    password: string,
    phoneNumber: number,
    profilePic: string // Added parameter for profile picture URL
  ) {
    this.userId = userId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.emailId = emailId;
    this.password = password;
    this.phoneNumber = phoneNumber;
    this.profilePic = profilePic; // Assign profilePic parameter to instance variable
  }
}
