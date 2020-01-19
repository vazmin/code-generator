import {Component} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {${table.upperCamelCaseName}Service} from './${table.lowerCaseSubName}.service';
import {AbstractManageDetailComponent} from '../../../shared/manage/abstract-manage-detail.component';

@Component({
    selector: 'ngx-${table.lowerCaseSubName}-detail',
    templateUrl: './${table.lowerCaseSubName}-detail.component.html'
})
export class ${table.upperCamelCaseName}DetailComponent extends AbstractManageDetailComponent {

    ${table.lowerCamelCaseName}: any;

    constructor(private ${table.lowerCamelCaseName}Service: ${table.upperCamelCaseName}Service,
                protected route: ActivatedRoute) {
        super(${table.lowerCamelCaseName}Service, route);
    }

    setData(data) {
        this.${table.lowerCamelCaseName} = data;
    }

}
