Feature: VALU3S app method and tool edit

    Scenario: user wants to create tool
      Given user is logged in as "Administrator"
       And user is on page Add tool
       And user filled all required fields tool
      When user click on button "save"
      Then user create tool

    Scenario: user wants to create method without require fields
      Given user is on page Add Method
      When user clicked on button "save"
      Then page shows error "Error  There were some errors."

    Scenario: user wants to create method related to tool
      Given user is on page Add Method
       And user filled all required fields method
       And user added relation to tool
      When user click on button "save"
      Then user create new method

    Scenario: user wants to edit method
      Given user is on page Edit method
       And user edited some field
      When user click on button "save"
      Then page shows Info "Changes saved"
       And user is transfered to modified method page

    Scenario: user wants to delete relation from method to tool
      Given user is on page Edit method
       And user is in section Relations
      When user click on button "x"
       And user click on button "save"
      Then user delete relation

    Scenario: user wants to rename method
      Given user is on page Method
       And user clicked on action "rename"
       And user filled new name
      When user click on button "rename"
      Then method is rename

    Scenario: user wants to publish method
      Given user is on page Method
      When user click on button published
      Then page shows info "Item state changed"
       And state of method changes to published

    Scenario: user wants to send back method to private state
      Given user is on page Method
      When user click on button "Send back"
      Then page shows info "Item state changed"
       And state of method changes to private

    Scenario: user wants to delete method
      Given user is on page method
       And user click on action delete
      When user click on button "delete"
      Then method is delete from methods


