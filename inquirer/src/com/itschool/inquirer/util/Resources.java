package com.itschool.inquirer.util;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class Resources {
	
   @Produces
   @PersistenceContext(unitName = "DAISInquirer")
   private EntityManager entityManager;
   
}
