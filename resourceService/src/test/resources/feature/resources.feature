Feature: Testing file saving and metada parsing

  Scenario: Resource saving
    When client calls post endpoint to save resource
    Then client calls song service to retrieve processed metadata

