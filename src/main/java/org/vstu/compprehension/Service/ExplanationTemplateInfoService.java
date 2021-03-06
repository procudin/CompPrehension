package org.vstu.compprehension.Service;

import org.vstu.compprehension.models.repository.ExplanationTemplateInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExplanationTemplateInfoService {
    private ExplanationTemplateInfoRepository explanationTemplateInfoRepository;

    @Autowired
    public ExplanationTemplateInfoService(ExplanationTemplateInfoRepository explanationTemplateInfoRepository) {
        this.explanationTemplateInfoRepository = explanationTemplateInfoRepository;
    }
}
