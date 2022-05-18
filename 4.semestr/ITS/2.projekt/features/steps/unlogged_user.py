from behave import *
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time


@given(u'user is on page Home')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo")


@given(u'user is unlogged')
def step_impl(context):
    pass


@when(u'user click on folder Methods')
def step_impl(context):
    context.driver.find_element(By.CSS_SELECTOR, ".odd:nth-child(1) .contenttype-folder").click()


@then(u'user see list of all method')
def step_impl(context):
    pass


@when(u'user click on folder tools')
def step_impl(context):
    context.driver.find_element(By.CSS_SELECTOR, ".even:nth-child(2) .contenttype-folder").click()


@then(u'user see list of all tool')
def step_impl(context):
    pass


@when(u'user click on folder Use Cases')
def step_impl(context):
    context.driver.find_element(By.CSS_SELECTOR, ".even:nth-child(4) .contenttype-folder").click()


@then(u'user see list of all use case')
def step_impl(context):
    pass


@given(u'user is on page Methods')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/method")


@when(u'user click on one method')
def step_impl(context):
    context.driver.find_element(By.LINK_TEXT, "Source Code Static Analysis").click()


@then(u'user see page for specific method')
def step_impl(context):
    pass


@given(u'user is in page Methods')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/method/source-code-static-analysis")


@given(u'user set method to private state')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/method/source-code-static-analysis")
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.CSS_SELECTOR, ".plone-toolbar-state-title"))).click()
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.ID, "workflow-transition-reject"))).click()


@when(u'user logs out')
def step_impl(context):
    time.sleep(1)
    context.driver.find_element(By.CSS_SELECTOR, "#portal-personaltools span:nth-child(2)").click()
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.ID, "personaltools-logout"))).click()


@then(u'user can not see method')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/method")
    assert EC.invisibility_of_element_located((By.LINK_TEXT, "Source Code Static Analysis"))


@given(u'user set method to publish state')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/method/source-code-static-analysis")
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.CSS_SELECTOR, ".plone-toolbar-title:nth-child(1)"))).click()
    WebDriverWait(context.driver, 30).until(EC.element_to_be_clickable((By.ID, "workflow-transition-publish"))).click()


@then(u'user can see method')
def step_impl(context):
    context.driver.get("http://localhost:8080/repo/method")
    assert context.driver.find_element(By.LINK_TEXT, "Source Code Static Analysis")
