package org.ei.opensrp.domain;

import com.google.gson.Gson;

import org.ei.opensrp.domain.form.FormData;
import org.ei.opensrp.domain.form.FormField;
import org.ei.opensrp.domain.form.FormInstance;
import org.ei.opensrp.domain.form.FormSubmission;
import org.ei.opensrp.domain.form.SubForm;
import org.ei.opensrp.util.FormSubmissionBuilder;
import org.robolectric.RobolectricTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
public class FormSubmissionTest {
    @Test
    public void shouldGetFieldValueByName() throws Exception {
        FormInstance formInstance = new FormInstance(new FormData("entity", "default", asList(new FormField("field1", "value1", "source1"), new FormField("field2", "value2", "source2")),
                asList(new SubForm("sub form name"))), "1");

        FormSubmission formSubmission = FormSubmissionBuilder.create().withFormInstance(new Gson().toJson(formInstance)).build();

        assertEquals("value1", formSubmission.getFieldValue("field1"));
        assertEquals("value2", formSubmission.getFieldValue("field2"));
        assertNull(formSubmission.getFieldValue("non existent field"));
    }

    @Test
    public void shouldGetSubFormByName() throws Exception {
        SubForm subForm = new SubForm("sub form name");
        FormInstance formInstance = new FormInstance(new FormData("entity", "default", asList(new FormField("field1", "value1", "source1"), new FormField("field2", "value2", "source2")),
                asList(subForm)), "1");

        FormSubmission formSubmission = FormSubmissionBuilder.create().withFormInstance(new Gson().toJson(formInstance)).build();

        assertEquals(subForm, formSubmission.getSubFormByName("sub form name"));
    }
}
