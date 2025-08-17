package com.bigco.org.neha;

import com.bigco.org.neha.dto.ChainIssueDTO;
import com.bigco.org.neha.dto.SalaryViolationDTO;
import java.util.List;

public record OrgAnalysisReport(
        List<SalaryViolationDTO> underpaid,
        List<SalaryViolationDTO> overpaid,
        List<ChainIssueDTO> longChains
) {}
