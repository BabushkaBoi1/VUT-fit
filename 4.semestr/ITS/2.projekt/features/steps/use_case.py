from behave import *
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time


@given(u'user is on page Add Use Case')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/use-cases#c4=all&b_start=0")
    time.sleep(1)
    context.driver.find_element(By.CSS_SELECTOR, "#plone-contentmenu-factories .plone-toolbar-title").click()
    context.driver.find_element(By.ID, "use_case").click()


@given(u'user fill all required fields')
def step_impl(context):
    context.driver.find_element(By.ID, "form-widgets-IBasic-title").click()
    context.driver.find_element(By.ID, "form-widgets-IBasic-title").send_keys("test")
    context.driver.switch_to.frame(0)
    context.driver.find_element(By.CSS_SELECTOR, "html").click()
    element = context.driver.find_element(By.ID, "tinymce")
    context.driver.execute_script("if(arguments[0].contentEditable === 'true') {arguments[0].innerText = 'test'}", element)
    context.driver.switch_to.default_content()


@given(u'user adds evaluatuion scenarios')
def step_impl(context):
    context.driver.find_element(By.ID, "autotoc-item-autotoc-1").click()
    context.driver.find_element(By.CSS_SELECTOR, "#formfield-form-widgets-evaluation_scenario .path-wrapper .glyphicon").click()
    context.driver.find_element(By.CSS_SELECTOR, ".contenttype-evaluation_tool_dimension").click()


@then(u'user creates Use Case')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/use-cases/test")


@given(u'user is on page Edit Use Case')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/use-cases/test")
    context.driver.find_element(By.CSS_SELECTOR, "#contentview-edit span:nth-child(2)").click()


@given(u'user is in section "Use case Evaluation Scenarios"')
def step_impl(context):
    context.driver.find_element(By.ID, "autotoc-item-autotoc-1").click()


@then(u'user deletes evaluation scenario form use case')
def step_impl(context):
    assert EC.invisibility_of_element_located((By.LINK_TEXT, "test"))


@given(u'user is on page Use case')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/use-cases/test")


@given(u'user click button "Add Evaluation scenario"')
def step_impl(context):
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.CSS_SELECTOR, ".icon-plone-contentmenu-factories"))).click()
    context.driver.find_element(By.ID, "evaluation_scenario").click()


@when(u'user fill all required fields')
def step_impl(context):
    context.driver.find_element(By.ID, "form-widgets-IBasic-title").click()
    context.driver.find_element(By.ID, "form-widgets-IBasic-title").send_keys("test")
    context.driver.find_element(By.ID, "form-widgets-evaluation_secnario_id").click()
    context.driver.find_element(By.ID, "form-widgets-evaluation_secnario_id").send_keys("test_ID")
    context.driver.find_element(By.ID, "form-widgets-evaluation_scenario_textual_description").click()
    context.driver.find_element(By.ID, "form-widgets-evaluation_scenario_textual_description").send_keys("test")


@then(u'user creates evaluation scenario to use case')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/use-cases/test/test")


@given(u'user is on page Edit Evaluation Scenario')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/use-cases/test/test")
    context.driver.find_element(By.CSS_SELECTOR, "#contentview-edit span:nth-child(2)").click()


@given(u'user is in section "Evaluation scenario Requirement"')
def step_impl(context):
    context.driver.find_element(By.ID, "autotoc-item-autotoc-1").click()


@when(u'user find requirements in searching field')
def step_impl(context):
    context.driver.find_element(By.CSS_SELECTOR, "#formfield-form-widgets-evaluation_scenario_requirements_list .path-wrapper .glyphicon").click()
    context.driver.find_element(By.CSS_SELECTOR, "#select2-result-label-8 .pattern-relateditems-result-path").click()


@then(u'user add requirements to evaluation scenario')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/use-cases/test/test")

    # clean up
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.CSS_SELECTOR, "#plone-contentmenu-actions .plone-toolbar-title"))).click()
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.ID, "plone-contentmenu-actions-delete"))).click()
    context.driver.find_element(By.CSS_SELECTOR, ".pattern-modal-buttons > #form-buttons-Delete").click()
    

@given(u'user click button add test case')
def step_impl(context):
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.CSS_SELECTOR, "#plone-contentmenu-factories .plone-toolbar-title"))).click()
    context.driver.find_element(By.ID, "test_case").click()


@when(u'user fill all required fields use case')
def step_imp(context):
    context.driver.find_element(By.ID, "form-widgets-IBasic-title").click()
    context.driver.find_element(By.ID, "form-widgets-IBasic-title").send_keys("test")
    context.driver.find_element(By.ID, "form-widgets-test_case_id").click()
    context.driver.find_element(By.ID, "form-widgets-test_case_id").send_keys("test")


@then(u'user creates Test Case')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/use-cases/test/test")


@given(u'user is on page Test case')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/use-cases/test/test")


@given(u'user click button "Add Requirement"')
def step_impl(context):
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.CSS_SELECTOR, "#plone-contentmenu-factories .plone-toolbar-title"))).click()
    context.driver.find_element(By.ID, "requirement").click()


@when(u'user fill all required fields requirement')
def step_impl(context):
    context.driver.find_element(By.ID, "form-widgets-IDublinCore-title").click()
    context.driver.find_element(By.ID, "form-widgets-IDublinCore-title").send_keys("test")


@then(u'user creates requirement to test case')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/use-cases/test/test/test")

    #clean up
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.CSS_SELECTOR, "#plone-contentmenu-actions .plone-toolbar-title"))).click()
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.ID, "plone-contentmenu-actions-delete"))).click()
    context.driver.find_element(By.CSS_SELECTOR, ".pattern-modal-buttons > #form-buttons-Delete").click()


@then(u'user rename use case')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/use-cases/test")


@when(u'user click on action delete')
def step_impl(context):
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.CSS_SELECTOR, "#plone-contentmenu-actions .plone-toolbar-title"))).click()
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.ID, "plone-contentmenu-actions-delete"))).click()


@then(u'user delete test case')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/use-cases/test")
    assert EC.invisibility_of_element_located((By.LINK_TEXT, "test"))

    #clean up
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.CSS_SELECTOR, "#plone-contentmenu-actions .plone-toolbar-title"))).click()
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.ID, "plone-contentmenu-actions-delete"))).click()
    context.driver.find_element(By.CSS_SELECTOR, ".pattern-modal-buttons > #form-buttons-Delete").click()
    time.sleep(1)
    context.driver.find_element(By.CSS_SELECTOR, "#portal-personaltools span:nth-child(2)").click()
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.ID, "personaltools-logout"))).click()