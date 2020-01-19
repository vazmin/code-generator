package ${TableNameControllerPackage};

import com.github.vazmin.framework.core.service.Pagination;
import com.github.vazmin.framework.core.service.ServiceProcessException;
import com.github.vazmin.framework.core.util.GsonUtils;
import com.github.vazmin.framework.core.util.tools.LongTools;
import com.github.vazmin.framework.web.support.annotation.Command;
import com.github.vazmin.framework.web.support.annotation.Module;
import com.github.vazmin.manage.component.controller.ManageControllerInterface;
import ${TableNamePackage}.${table.upperCamelCaseName};
import ${TableNameServicePackage}.${table.upperCamelCaseName}Service;
import com.github.vazmin.manage.component.controller.errors.BadRequestAlertException;
import com.github.vazmin.manage.support.query.Query;
import com.github.vazmin.manage.support.util.HeaderUtil;
import com.github.vazmin.manage.support.util.PaginationUtil;
import com.github.vazmin.manage.support.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


/**
 * ${table.clearRemark} 管理控制器类。
 * Created by ${app.author} on ${genDate}.
 */
@Module(value = ${table.upperCamelCaseName}Controller.MODULE_NAME, order = 3)
@RestController
@RequestMapping(${table.upperCamelCaseName}Controller.URL_PREFIX)
public class ${table.upperCamelCaseName}Controller implements ManageControllerInterface {
    private static final Logger log = LoggerFactory.getLogger(${table.upperCamelCaseName}Controller.class);

    public static final String MODULE_NAME = "${table.clearRemark}";
    public static final String URL_PREFIX = "${app.url.backend}";

    @Autowired
    private ${table.upperCamelCaseName}Service ${table.lowerCamelCaseName}Service;


    /**
     * GET /list : get ${table.upperCamelCaseName} list
     *
     * @param pagination to paging
     * @param query params
     * @return the ResponseEntity with status 200 (OK) and with body the ${table.upperCamelCaseName} list
     */
    @Command(MODULE_NAME + " list")
    @RequestMapping(value = LIST_URL, method = RequestMethod.GET)
    public ResponseEntity list(Pagination pagination, Query query) {

        List<${table.upperCamelCaseName}> ${table.lowerCamelCaseName}List
                = ${table.lowerCamelCaseName}Service.getList(pagination, query.getFilter());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pagination, URL_PREFIX);
        return new ResponseEntity<>(${table.lowerCamelCaseName}List, headers, HttpStatus.OK);
    }

    /**
    * PUT /edit : Updates an existing ${table.upperCamelCaseName}.
    *
    * @param ${table.lowerCamelCaseName} the ${table.upperCamelCaseName} to update
    * @return the ResponseEntity with status 200 (OK) and with body the updated ${table.upperCamelCaseName}
    */
    @Command(MODULE_NAME + " edit")
    @RequestMapping(value = EDIT_URL, method = RequestMethod.PUT)
    public ResponseEntity<Integer> update(@Valid @RequestBody ${table.upperCamelCaseName} ${table.lowerCamelCaseName})
             throws ServiceProcessException {
        int res = ${table.lowerCamelCaseName}Service.update(${table.lowerCamelCaseName});

        return ResponseUtil.wrapOrNotFound(res,
            HeaderUtil.createEntityUpdateAlert(${table.lowerCamelCaseName}.getId().toString()));
    }

    /**
    * POST /new : Creates a new ${table.upperCamelCaseName}.
    *
    * @param ${table.lowerCamelCaseName} the ${table.lowerCamelCaseName} to create
    * @return the ResponseEntity with status 200 (OK) and with body the created ${table.lowerCamelCaseName}
    * @throws ServiceProcessException 业务处理异常
    * @throws URISyntaxException url syntax Exception
    */
    @Command(MODULE_NAME + " new")
    @RequestMapping(value = NEW_URL, method = RequestMethod.POST)
    public ResponseEntity<${table.upperCamelCaseName}> newUser(@Valid @RequestBody ${table.upperCamelCaseName} ${table.lowerCamelCaseName})
        throws ServiceProcessException, URISyntaxException {
        if (${table.lowerCamelCaseName}.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "${table.lowerCamelCaseName}", "idexists");
            // Lowercase the${table.upperCamelCaseName} before comparing with database
        } else {
            Integer res = ${table.lowerCamelCaseName}Service.insert(${table.lowerCamelCaseName});
            return ResponseEntity.created(new URI(URL_PREFIX + "${table.lowerCaseSubName}/detail?id=" + ${table.lowerCamelCaseName}.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(${table.lowerCamelCaseName}.getId().toString()))
            .body(${table.lowerCamelCaseName});
        }
    }

    /**
    * GET /detail?id={id} : get the "id" ${table.lowerCamelCaseName}.
    *
    * @param id the id of the ${table.lowerCamelCaseName} to find
    * @return the ResponseEntity with status 200 (OK) and with body the "id" ${table.lowerCamelCaseName}, or with status 404 (Not Found)
    */
    @Command(MODULE_NAME + " detail")
    @RequestMapping(value = DETAIL_URL, method = RequestMethod.GET)
    public ResponseEntity<${table.upperCamelCaseName}> get(@RequestParam Long id) {
        return ResponseUtil.wrapOrNotFound(Optional.of(${table.lowerCamelCaseName}Service.get(id)));
    }

    /**
    * DELETE /delete?ids={ids} : delete id in the "ids" ${table.upperCamelCaseName}.
    *
    * @param ids id in the ids of the ${table.upperCamelCaseName} to delete
    * @return the ResponseEntity with status 200 (OK)
    */
    @Command(MODULE_NAME + " delete")
    @RequestMapping(value = DELETE_URL, method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestParam String ids) throws ServiceProcessException {
        ${table.lowerCamelCaseName}Service.batchDelete(LongTools.parseList(ids));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ids)).build();
    }


}
