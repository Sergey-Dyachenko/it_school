package com.itschool.inquirer.service;

import org.picketlink.authorization.annotations.LoggedIn;

import javax.ejb.Stateless;
import javax.ws.rs.Path;

@Path("/private/person")
@Stateless
@LoggedIn
public class PersonService {
/*
    @Inject
    private EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> getAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Person> criteria = cb.createQuery(Person.class);
        Root<Person> person = criteria.from(Person.class);

        criteria.select(person).orderBy(cb.asc(person.get("firstName")));

        return em.createQuery(criteria).getResultList();

    }
    */
}
