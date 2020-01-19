import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {AbstractManageService} from '../../../shared/manage/abstract-manage.service';

@Injectable()
export class ${table.upperCamelCaseName}Service extends AbstractManageService {

    constructor(protected http: HttpClient) {
        super(http);
        this.CREATE_URL = '${app.url.backend}/new';
        this.UPDATE_URL = '${app.url.backend}/edit';
        this.DETAIL_URL = '${app.url.backend}/detail';
        this.DELETE_URL = '${app.url.backend}/delete';
    }

}
