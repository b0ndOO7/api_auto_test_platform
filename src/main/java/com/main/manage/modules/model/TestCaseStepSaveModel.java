package com.main.manage.modules.model;

import com.main.manage.modules.entity.TestCaseStepEntity;

public class TestCaseStepSaveModel {

    private int caseId;
    private int[] apiIds;
    private TestCaseStepEntity[] testCaseSteps;

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public int[] getApiIds() {
        return apiIds;
    }

    public void setApiIds(int[] apiIds) {
        this.apiIds = apiIds;
    }

    public TestCaseStepEntity[] getTestCaseSteps() {
        return testCaseSteps;
    }

    public void setTestCaseSteps(TestCaseStepEntity[] testCaseSteps) {
        this.testCaseSteps = testCaseSteps;
    }
}
