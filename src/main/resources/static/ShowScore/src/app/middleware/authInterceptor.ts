import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Get the token directly from localStorage
  const token = localStorage.getItem('authToken');
  console.log('Token extracted:', token); // DEBUG

  // If a token exists, add it to the request headers
  if (token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    console.log('Request with auth header:', authReq.headers.get('Authorization')); // DEBUG
    return next(authReq);
  }

  return next(req);
};
