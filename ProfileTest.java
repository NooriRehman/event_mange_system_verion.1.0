/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;



/**
 *
 * @author noori
 */
public class ProfileTest {
    private ProfileTest classUnderTest; // Replace 'MyClassUnderTest' with your actual class name
    private JTextField fullNameFieldMock;
    private JTextField emailFieldMock;
    private JTextField contactFieldMock;

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    void setUp() throws Exception {
        // Initialize the class and mocks
        classUnderTest = new ProfileTest();

        fullNameFieldMock = mock(JTextField.class);
        emailFieldMock = mock(JTextField.class);
        contactFieldMock = mock(JTextField.class);

        // Mock the database connection and prepared statement
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);

        // Replace text fields with mocks in your class
        classUnderTest.fullNameField = fullNameFieldMock;
        classUnderTest.emailField = emailFieldMock;
        classUnderTest.contactField = contactFieldMock;

        // Mock the DatabaseConnection class
        DatabaseConnection mockDBConnection = mock(DatabaseConnection.class);
        when(mockDBConnection.getInstance().getConnection()).thenReturn(mockConnection);
    }

    @Test
    void testActionPerformed_AllFieldsValid() throws Exception {
        // Simulate valid input fields
        when(fullNameFieldMock.getText()).thenReturn("John Doe");
        when(emailFieldMock.getText()).thenReturn("john.doe@example.com");
        when(contactFieldMock.getText()).thenReturn("1234567890");

        // Mock PreparedStatement behavior
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // Simulate successful update

        // Simulate button click
        ActionEvent event = mock(ActionEvent.class);
        ProfileTest.SetProfileButtonListenerActionPerformed(event);

        // Verify input clearing after success
        verify(fullNameFieldMock).setText("");
        verify(emailFieldMock).setText("");
        verify(contactFieldMock).setText("");

        // Verify the database interaction
        verify(mockConnection).prepareStatement("INSERT INTO set_profile (full_name, email, contact_no) VALUES (?, ?, ?)");
        verify(mockPreparedStatement).setString(1, "John Doe");
        verify(mockPreparedStatement).setString(2, "john.doe@example.com");
        verify(mockPreparedStatement).setString(3, "1234567890");
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void testActionPerformed_MissingFields() {
        // Simulate missing input fields
        when(fullNameFieldMock.getText()).thenReturn("");
        when(emailFieldMock.getText()).thenReturn("");
        when(contactFieldMock.getText()).thenReturn("");

        ActionEvent event = mock(ActionEvent.class);
        classUnderTest.SetProfileButtonListenerActionPerformed(event);

        // Ensure no database interaction occurs
        verifyNoInteractions(mockConnection);
        verifyNoInteractions(mockPreparedStatement);
    }

    @Test
    void testActionPerformed_DatabaseError() throws Exception {
        // Simulate valid input fields
        when(fullNameFieldMock.getText()).thenReturn("John Doe");
        when(emailFieldMock.getText()).thenReturn("john.doe@example.com");
        when(contactFieldMock.getText()).thenReturn("1234567890");

        // Mock database failure
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        ActionEvent event = mock(ActionEvent.class);
        assertThrows(RuntimeException.class, () -> {
            classUnderTest.SetProfileButtonListenerActionPerformed(event);
        });

        // Verify that no fields are cleared on failure
        verify(fullNameFieldMock, never()).setText("");
        verify(emailFieldMock, never()).setText("");
        verify(contactFieldMock, never()).setText("");
    }
    
    public ProfileTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }


    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
