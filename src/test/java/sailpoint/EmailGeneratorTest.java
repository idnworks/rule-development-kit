package sailpoint;

import bsh.EvalError;
import bsh.Interpreter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import sailpoint.object.Application;
import sailpoint.object.Identity;
import sailpoint.rdk.utils.RuleXmlUtils;
import sailpoint.server.IdnRuleUtil;
import sailpoint.tools.GeneralException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmailGeneratorTest {
    Logger log = LogManager.getLogger(EmailGeneratorTest.class);

    private static final String RULE_FILENAME = "src/main/resources/rules/Rule - AttributeGenerator - EmailGenerator.xml";

    @Test
    public void testUsernameGeneratorWhereFirstAndLastNameValid () throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        IdnRuleUtil idn = mock();
        when(idn.accountExistsByDisplayName(any(), any())).thenReturn(false);
        when(idn.attrSearchGetIdentityName(any(), any(), any(), any())).thenReturn(null);
        when(idn.isUniqueLDAPValue(any(), any(), any(), any())).thenReturn(true);

        Application application = mock(Application.class);
        when(application.getName()).thenReturn("Active Directory [source]");

        Identity identity = mock(Identity.class);
        when(identity.getFirstname()).thenReturn("tyler");
        when(identity.getLastname()).thenReturn("Smith");
        when(identity.getAttribute("companyId")).thenReturn("100");
        when(identity.getId()).thenReturn("g12h3b1g2y3v12");
        String result = "";

        i.set("log", log);
        i.set("idn", idn);
        i.set("application", application);
        i.set("identity", identity);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);
        result = (String) i.eval(source);

        assertNotNull(result);
        assertEquals(result, "tyler.smith@jmfamily.com");

        log.info("Beanshell script returned: " + result);

    }
    @Test
    public void testUsernameGeneratorWithDifferentCompanyID() throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        IdnRuleUtil idn = mock(IdnRuleUtil.class);
        when(idn.accountExistsByDisplayName(any(), any())).thenReturn(false);
        when(idn.attrSearchGetIdentityName(any(), any(), any(), any())).thenReturn(null);
        when(idn.isUniqueLDAPValue(any(), any(), any(), any())).thenReturn(true);

        Application application = mock(Application.class);
        when(application.getName()).thenReturn("Active Directory [source]");

        Identity identity = mock(Identity.class);
        when(identity.getFirstname()).thenReturn("Alice");
        when(identity.getLastname()).thenReturn("Johnson");
        when(identity.getAttribute("companyId")).thenReturn("300"); // Different Company ID
        when(identity.getId()).thenReturn("a1b2c3d4");
        String result = "";

        i.set("log", log);
        i.set("idn", idn);
        i.set("application", application);
        i.set("identity", identity);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);
        result = (String) i.eval(source);

        // The domain should be "jmagroup.com" based on company ID "300"
        assertNotNull(result);
        assertEquals(result, "alice.johnson@jmagroup.com");

        log.info("Beanshell script returned: " + result);
    }
    @Test
    public void testUsernameGeneratorWithNullFirstName() throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        IdnRuleUtil idn = mock(IdnRuleUtil.class);
        when(idn.accountExistsByDisplayName(any(), any())).thenReturn(false);
        when(idn.attrSearchGetIdentityName(any(), any(), any(), any())).thenReturn(null);
        when(idn.isUniqueLDAPValue(any(), any(), any(), any())).thenReturn(true);

        Application application = mock(Application.class);
        when(application.getName()).thenReturn("Active Directory [source]");

        Identity identity = mock(Identity.class);
        when(identity.getFirstname()).thenReturn(""); // Null or empty first name
        when(identity.getLastname()).thenReturn("Smith");
        when(identity.getAttribute("companyId")).thenReturn("100");
        when(identity.getId()).thenReturn("g12h3b1g2y3v12");
        String result = "";

        i.set("log", log);
        i.set("idn", idn);
        i.set("application", application);
        i.set("identity", identity);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);
        result = (String) i.eval(source);

        // Since the first name is empty, it may throw an error or return null based on your logic
        assertNotNull(result); // Could be a fail if your system requires valid first name
        log.info("Beanshell script returned: " + result);
    }
    @Test
    public void testUsernameGeneratorWithNullLastName() throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        IdnRuleUtil idn = mock(IdnRuleUtil.class);
        when(idn.accountExistsByDisplayName(any(), any())).thenReturn(false);
        when(idn.attrSearchGetIdentityName(any(), any(), any(), any())).thenReturn(null);
        when(idn.isUniqueLDAPValue(any(), any(), any(), any())).thenReturn(true);

        Application application = mock(Application.class);
        when(application.getName()).thenReturn("Active Directory [source]");

        Identity identity = mock(Identity.class);
        when(identity.getFirstname()).thenReturn("John");
        when(identity.getLastname()).thenReturn(""); // Null or empty last name
        when(identity.getAttribute("companyId")).thenReturn("100");
        when(identity.getId()).thenReturn("g12h3b1g2y3v12");
        String result = "";

        i.set("log", log);
        i.set("idn", idn);
        i.set("application", application);
        i.set("identity", identity);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);
        result = (String) i.eval(source);

        // Since the last name is empty, it may throw an error or return null based on your logic
        assertNotNull(result); // Could be a fail if your system requires valid last name
        log.info("Beanshell script returned: " + result);
    }
    @Test
    public void testUsernameGeneratorWithInvalidCompanyID() throws GeneralException, EvalError {
        Interpreter i = new Interpreter();

        IdnRuleUtil idn = mock(IdnRuleUtil.class);
        when(idn.accountExistsByDisplayName(any(), any())).thenReturn(false);
        when(idn.attrSearchGetIdentityName(any(), any(), any(), any())).thenReturn(null);
        when(idn.isUniqueLDAPValue(any(), any(), any(), any())).thenReturn(true);

        Application application = mock(Application.class);
        when(application.getName()).thenReturn("Active Directory [source]");

        Identity identity = mock(Identity.class);
        when(identity.getFirstname()).thenReturn("Samantha");
        when(identity.getLastname()).thenReturn("Taylor");
        when(identity.getAttribute("companyId")).thenReturn("999"); // Invalid company ID
        when(identity.getId()).thenReturn("f7h4g8b6v2j9z8");
        String result = "";

        i.set("log", log);
        i.set("idn", idn);
        i.set("application", application);
        i.set("identity", identity);

        String source = RuleXmlUtils.readRuleSourceFromFilePath(RULE_FILENAME);
        result = (String) i.eval(source);

        // The company ID "999" doesn't exist in the mapping, so it should return "CompanyID not found!"
        assertNotNull(result);
        assertEquals(result, "samantha.taylor@CompanyID not found!");

        log.info("Beanshell script returned: " + result);
    }




   
}
