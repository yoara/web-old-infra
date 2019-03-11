package com.company.seed.module.user.solr.spring;

import com.company.seed.module.user.model.UserModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.repository.Highlight;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

/**
 * Created by yoara on 2016/8/9.
 */
public interface UserRepositoryService extends SolrCrudRepository<UserModel, String> {
    List<UserModel> findByName(String name);

    @Highlight(prefix = "<b>", postfix = "</b>")
    HighlightPage<UserModel> findByName(String name, Pageable page);
}
