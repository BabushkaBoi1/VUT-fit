from behave import *
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time


@given(u'user is logged in as "Administrator"')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo")
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.ID, "personaltools-login"))).click()
    context.driver.find_element(By.ID, "__ac_name").click()
    context.driver.find_element(By.ID, "__ac_name").send_keys("administrator")
    context.driver.find_element(By.ID, "__ac_password").send_keys("administrator")
    context.driver.find_element(By.CSS_SELECTOR, ".pattern-modal-buttons > #buttons-login").click()
    time.sleep(1)


@given(u'user is on page Add tool')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/tools")
    context.driver.find_element(By.CSS_SELECTOR, "#plone-contentmenu-factories .plone-toolbar-title").click()
    context.driver.find_element(By.ID, "tool").click()


@given(u'user filled all required fields tool')
def step_impl(context):
    context.driver.find_element(By.ID, "form-widgets-IDublinCore-title").click()
    context.driver.find_element(By.ID, "form-widgets-IDublinCore-title").send_keys("test")
    context.driver.find_element(By.ID, "form-widgets-tool_purpose").click()
    context.driver.find_element(By.ID, "form-widgets-tool_purpose").send_keys("test")
    context.driver.switch_to.frame(3)
    context.driver.find_element(By.CSS_SELECTOR, "html").click()
    context.driver.switch_to.default_content()
    context.driver.execute_script("window.scrollTo(0,1456.5833740234375)")
    context.driver.switch_to.frame(3)
    element = context.driver.find_element(By.ID, "tinymce")
    context.driver.execute_script(
        "if(arguments[0].contentEditable === 'true') {arguments[0].innerText = 'test'}",
        element)
    context.driver.switch_to.default_content()
    context.driver.switch_to.frame(2)
    context.driver.find_element(By.CSS_SELECTOR, "html").click()
    element = context.driver.find_element(By.ID, "tinymce")
    context.driver.execute_script(
        "if(arguments[0].contentEditable === 'true') {arguments[0].innerText = 'test'}",
        element)
    context.driver.switch_to.default_content()
    context.driver.switch_to.frame(1)
    context.driver.find_element(By.CSS_SELECTOR, "html").click()
    element = context.driver.find_element(By.ID, "tinymce")
    context.driver.execute_script(
        "if(arguments[0].contentEditable === 'true') {arguments[0].innerText = 'test'}",
        element)
    context.driver.switch_to.default_content()


@given(u'user filled all required fields method')
def step_impl(context):
    context.driver.find_element(By.ID, "form-widgets-IBasic-title").click()
    context.driver.find_element(By.ID, "form-widgets-IBasic-title").send_keys("test")
    context.driver.find_element(By.ID, "form-widgets-method_purpose").click()
    context.driver.find_element(By.ID, "form-widgets-method_purpose").send_keys("test")
    context.driver.switch_to.frame(3)
    context.driver.find_element(By.CSS_SELECTOR, "html").click()
    element = context.driver.find_element(By.ID, "tinymce")
    context.driver.execute_script(
        "if(arguments[0].contentEditable === 'true') {arguments[0].innerText = 'test'}",
        element)
    context.driver.switch_to.default_content()
    context.driver.switch_to.frame(2)
    context.driver.find_element(By.CSS_SELECTOR, "html").click()
    element = context.driver.find_element(By.ID, "tinymce")
    context.driver.execute_script(
        "if(arguments[0].contentEditable === 'true') {arguments[0].innerText = 'test'}",
        element)
    context.driver.switch_to.default_content()
    context.driver.switch_to.frame(1)
    context.driver.find_element(By.CSS_SELECTOR, "html").click()
    element = context.driver.find_element(By.ID, "tinymce")
    context.driver.execute_script(
        "if(arguments[0].contentEditable === 'true') {arguments[0].innerText = 'test'}",
        element)
    context.driver.switch_to.default_content()


@when(u'user click on button "save"')
def step_impl(context):
    context.driver.find_element(By.ID, "form-buttons-save").click()


@then(u'user create tool')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/tools/test")


@given(u'user is on page Add Method')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/method/++add++method")


@when(u'user clicked on button "save"')
def step_impl(context):
    context.driver.find_element(By.ID, "form-buttons-save").click()


@then(u'page shows error "Error  There were some errors."')
def step_impl(context):
    assert context.driver.find_element(By.CSS_SELECTOR, ".portalMessage")


@given(u'user added relation to tool')
def step_impl(context):
    context.driver.find_element(By.ID, "autotoc-item-autotoc-2").click()
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.CSS_SELECTOR, "#formfield-form-widgets-tools .path-wrapper .glyphicon"))).click()
    context.driver.find_element(By.ID, "visual-portal-wrapper").click()
    context.driver.find_element(By.ID, "visual-portal-wrapper").send_keys("test")
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.CSS_SELECTOR, ".pattern-relateditems-result-title"))).click()


@then(u'user create new method')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/method")


@given(u'user is on page Edit method')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/method/test/edit?_authenticator=9d4f695d614b6bc4aba93154012ddec16900a083")


@given(u'user edited some field')
def step_impl(context):
    context.driver.find_element(By.ID, "form-widgets-method_purpose").click()
    context.driver.find_element(By.ID, "form-widgets-method_purpose").send_keys("testing")


@then(u'page shows Info "Changes saved"')
def step_impl(context):
    assert context.driver.find_element(By.CSS_SELECTOR, ".portalMessage")


@then(u'user is transfered to modified method page')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/method/test")


@given(u'user is in section Relations')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/method/test")
    context.driver.find_element(By.CSS_SELECTOR, "#contentview-edit span:nth-child(2)").click()
    element = context.driver.find_element(By.CSS_SELECTOR, "#contentview-view span:nth-child(2)")
    actions = ActionChains(context.driver)
    actions.move_to_element(element).perform()
    context.driver.find_element(By.ID, "autotoc-item-autotoc-2").click()


@when(u'user click on button "x"')
def step_impl(context):
    context.driver.find_element(By.CSS_SELECTOR, ".select2-search-choice-close").click()


@then(u'user delete relation')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/method/test")


@given(u'user is on page Method')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/method/test")


@given(u'user clicked on action "rename"')
def step_impl(context):
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.CSS_SELECTOR, "#plone-contentmenu-actions .plone-toolbar-title"))).click()
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.ID, "plone-contentmenu-actions-rename"))).click()


@given(u'user filled new name')
def step_impl(context):
    context.driver.find_element(By.ID, "form-widgets-new_title").click()
    context.driver.find_element(By.ID, "form-widgets-new_title").send_keys("ing")


@when(u'user click on button "rename"')
def step_impl(context):
    context.driver.find_element(By.CSS_SELECTOR, ".pattern-modal-buttons > #form-buttons-Rename").click()


@then(u'method is rename')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/method")
    assert context.driver.find_element(By.LINK_TEXT, "testing")


@when(u'user click on button published')
def step_impl(context):
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.CSS_SELECTOR, ".plone-toolbar-title:nth-child(1)"))).click()
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.ID, "workflow-transition-publish"))).click()


@then(u'page shows info "Item state changed"')
def step_impl(context):
    assert context.driver.find_element(By.CSS_SELECTOR, ".portalMessage")


@then(u'state of method changes to published')
def step_impl(context):
    assert context.driver.find_element(By.CSS_SELECTOR, ".label-state-published > span:nth-child(2)")


@when(u'user click on button "Send back"')
def step_impl(context):
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.CSS_SELECTOR, ".plone-toolbar-title:nth-child(1)"))).click()
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.ID, "workflow-transition-reject"))).click()


@then(u'state of method changes to private')
def step_impl(context):
    assert context.driver.find_element(By.CSS_SELECTOR, ".label-state-private > span:nth-child(2)")


@given(u'user click on action delete')
def step_impl(context):
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.CSS_SELECTOR, "#plone-contentmenu-actions .plone-toolbar-title"))).click()
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.ID, "plone-contentmenu-actions-delete"))).click()


@when(u'user click on button "delete"')
def step_impl(context):
    context.driver.find_element(By.CSS_SELECTOR, ".pattern-modal-buttons > #form-buttons-Delete").click()


@then(u'method is delete from methods')
def step_impl(context):
    assert EC.invisibility_of_element_located((By.LINK_TEXT, "test"))

    # clean up tool
    context.driver.get("http://localhost:8080/repo/tools/test")
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.CSS_SELECTOR, "#plone-contentmenu-actions .plone-toolbar-title"))).click()
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.ID, "plone-contentmenu-actions-delete"))).click()
    context.driver.find_element(By.CSS_SELECTOR, ".pattern-modal-buttons > #form-buttons-Delete").click()
    time.sleep(1)
    context.driver.find_element(By.CSS_SELECTOR, "#portal-personaltools span:nth-child(2)").click()
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.ID, "personaltools-logout"))).click()

