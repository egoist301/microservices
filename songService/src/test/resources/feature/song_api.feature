Feature: Song Service API
  As a user
  I want to be able to save song information and retrieve it by id

  Scenario: Add song to service
    Given user's song
    When user uploads song
    Then save song
    And return song id

  Scenario: Get song from service
    Given pre-saved song
    When user requests song by id
    Then return song

  Scenario: Delete songs from service
    Given pre-saved songs
    When user request delete songs
    Then return deleted songs ids