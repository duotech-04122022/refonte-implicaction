import {Injectable} from '@angular/core';
import {ApiHttpService} from '../../core/services/api-http.service';
import {ApiEndpointsService} from '../../core/services/api-endpoints.service';
import {Observable} from 'rxjs';
import {User} from '../models/user';
import {Pageable} from '../models/pageable';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private apiHttpService: ApiHttpService,
    private apiEndpointsService: ApiEndpointsService
  ) {

  }

  getAll(pageable: Pageable): Observable<Pageable<User>> {
    return (this.apiHttpService
      .get(this.apiEndpointsService.getAllUserEndpoint(pageable)) as Observable<Pageable<User>>);
  }
}