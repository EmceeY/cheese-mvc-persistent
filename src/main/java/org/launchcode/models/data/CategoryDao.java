package org.launchcode.models.data;

import org.launchcode.models.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by User on 4/12/17.
 */

@Repository
@Transactional
public interface CategoryDao extends CrudRepository <Category, Integer> {

}
