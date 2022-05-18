Feature: VALU3S app use case edit


  Scenario: user wants to create Use Case with evaluation scenarios
    Given user is logged in as "Administrator"
     And user is on page Add Use Case
     And user fill all required fields
     And user adds evaluatuion scenarios
    When user click on button "save"
    Then user creates Use Case

  Scenario: user wants to delete evaluation scenario from use case
    Given user is on page Edit Use Case
     And user is in section "Use case Evaluation Scenarios"
    When user click on button "x"
     And user click on button "save"
    Then user deletes evaluation scenario form use case

  Scenario: user wants to create evaluation scenario to use case contest
    Given user is on page Use case
     And user click button "Add Evaluation scenario"
    When user fill all required fields
    And user click on button "save"
    Then user creates evaluation scenario to use case

  Scenario: user wants to add Requiremet to evaluation scenario
    Given user is on page Edit Evaluation Scenario
     And user is in section "Evaluation scenario Requirement"
    When user find requirements in searching field
     And user click on button "save"
    Then user add requirements to evaluation scenario

  Scenario: user wants to create test case to use case contest
    Given user is on page Use case
     And user click button add test case
    When user fill all required fields use case
     And user click on button "save"
    Then user creates Test Case

  Scenario: user wants to create requirement to test case contest
    Given user is on page Test case
     And user click button "Add Requirement"
    When user fill all required fields requirement
     And user click on button "save"
    Then user creates requirement to test case

  Scenario: user wants to rename use case
    Given user is on page Use case
     And user clicked on action "rename"
     And user filled new name
    When user click on button "rename"
    Then user rename use case

  Scenario: user wants to delete test case
    Given user is on page Test case
    When user click on action delete
     And user click on button "delete"
    Then user delete test case
