Feature: VALU3S for unlogged user view

  Scenario Outline: user wants to see methods
    Given user is on page Home
     And user is unlogged
    When user click on folder <folder>
    Then user see list of all <item>
  Examples:
    |folder   |item    |
    |Methods  |method  |
    |tools    |tool    |
    |Use Cases|use case|

  Scenario: user wants to see specific method
    Given user is on page Methods
     And user is unlogged
    When user click on one method
    Then user see page for specific method

  Scenario: user wants see method in state private
    Given user is in page Methods
     And user is logged in as Administrator
     And user set method to private state
    When user logs out
    Then user can not see method

  Scenario: user wants see method in state publish
    Given user is in page Methods
     And user is logged in as Administrator
     And user set method to publish state
    When user logs out
    Then user can see method