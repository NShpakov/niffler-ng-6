package guru.qa.niffler.data.dao.dao;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

public interface AuthAuthorityDao {

    void create(AuthorityEntity... authority);
}