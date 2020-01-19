import {Component} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {${table.upperCamelCaseName}Service} from './${table.lowerCaseSubName}.service';
import {AbstractManageEditComponent} from '../../../shared/manage/abstract-manage-edit.component';

@Component({
    selector: 'ngx-${table.lowerCaseSubName}-edit',
    templateUrl: './${table.lowerCaseSubName}-edit.component.html',
})
export class ${table.upperCamelCaseName}EditComponent extends AbstractManageEditComponent {

    ${table.lowerCamelCaseName}: any;

    constructor(private ${table.lowerCamelCaseName}Service: ${table.upperCamelCaseName}Service,
                protected router: Router,
                protected route: ActivatedRoute) {
        super(${table.lowerCamelCaseName}Service, router, route);
    }

    setData(data) {
        this.${table.lowerCamelCaseName} = data;
    }

    getData() {
        return this.${table.lowerCamelCaseName};
    }

}
