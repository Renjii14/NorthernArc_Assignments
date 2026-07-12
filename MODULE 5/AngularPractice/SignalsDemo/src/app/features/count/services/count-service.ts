import {Injectable, Service, signal, WritableSignal} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CountServices {

  private count:WritableSignal<number>=signal(0);
  
  getCount():WritableSignal<number> {
    return this.count;
  }
  incrementCount(){
    return this.count.set(this.count() + 1);
  }
  decrementCount(){
    return this.count.set(this.count() - 1);
  }
  incrementBy(value:number){
    return this.count.set(this.count() + value);
  }
  decrementBy(value:number){
    return this.count.set(this.count() - value);
  }
  
}
 