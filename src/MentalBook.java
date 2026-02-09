// Welcome to the Mental Book, a simple Java program that serves as a digital journal for your thoughts and feelings. This program will allow you to input your thoughts and feelings, and it will store them for you to review later. Let's get started!

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class MentalBook
{
    public static void main(String[] args) throws Exception 
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Xander: Hello, I am Xander, your mental book. I will help you to keep track of your thoughts and feelings. Please tell me what you'd like to focus on today.");
        scanner.nextLine();
        while (true)
      {
        System.out.print("You: ");
            String userInput = scanner.nextLine();

        if (userInput.equalsIgnoreCase("exit")) 
        {
            System.out.println("Xander: Goodbye!");
            break;
        }

        String userInput = scanner.nextLine();
        System.out.println("Xander: Thank you for sharing. I will keep this in mind as we continue our conversation. Is there anything else you'd like to share with me?");
        scanner.nextLine();
      }
    }
  }