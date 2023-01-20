/**
 * 
 */
package com.promineotech.jeep.service;

import com.promineotech.jeep.dao.JeepOrderDao;
import com.promineotech.jeep.entity.Order;
import com.promineotech.jeep.entity.OrderRequest;

/**
 * @author Bryce Cash
 *
 */
public interface JeepOrderService {

	Order createOrder(OrderRequest orderRequest);
	
}
