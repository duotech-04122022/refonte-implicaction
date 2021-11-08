package com.dynonuggets.refonteimplicaction.controller;

import com.dynonuggets.refonteimplicaction.dto.CompanyDto;
import com.dynonuggets.refonteimplicaction.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CompanyController.class)
class CompanyControllerTest extends ControllerIntegrationTestBase {

    private final String BASE_URI = "/api/companies";

    List<CompanyDto> companyDtos;

    @MockBean
    CompanyService companyService;

    @BeforeEach
    protected void setUp() {
        companyDtos = Arrays.asList(
                CompanyDto.builder().id(1L).description("description").name("Idemia").logo("logo").url("url").build(),
                CompanyDto.builder().id(2L).description("description2").name("SG").logo("logo2").url("url2").build());
    }

    @WithMockUser
    @Test
    void getCompanysListShouldListAllCompanies() throws Exception {
        Page<CompanyDto> companyDtoPage = new PageImpl<>(companyDtos);

        // test des données de pagination
        given(companyService.getAll(DEFAULT_PAGEABLE)).willReturn(companyDtoPage);
        ResultActions actions = mvc.perform(get(BASE_URI).contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(companyDtoPage.getTotalPages()))
                .andExpect(jsonPath("$.totalElements").value(companyDtos.size()));

        // test des propriétés de chaque éléments de la liste reçue
        for (int i = 0; i < companyDtos.size(); i++) {
            final String contentPath = String.format("$.content[%d]", i);
            actions.andExpect(jsonPath(contentPath + ".id", is(Math.toIntExact(companyDtos.get(i).getId()))))
                    .andExpect(jsonPath(contentPath + ".description", is(companyDtos.get(i).getDescription())))
                    .andExpect(jsonPath(contentPath + ".name", is(companyDtos.get(i).getName())))
                    .andExpect(jsonPath(contentPath + ".logo", is(companyDtos.get(i).getLogo())))
                    .andExpect(jsonPath(contentPath + ".url", is(companyDtos.get(i).getUrl())));
        }

        verify(companyService, times(1)).getAll(any());
    }

    @Test
    void getAllWithoutJwtShouldBeForbidden() throws Exception {
        mvc.perform(get(BASE_URI).contentType(APPLICATION_JSON)).andDo(print())
                .andExpect(status().isForbidden());

        verify(companyService, times(0)).getAll(any());
    }
}