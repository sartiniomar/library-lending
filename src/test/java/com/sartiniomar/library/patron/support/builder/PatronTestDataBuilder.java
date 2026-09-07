package com.sartiniomar.library.patron.support.builder;

import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.domain.patron.PatronType;
import java.util.UUID;

public class PatronTestDataBuilder {
  public Patron buildDefaultRegular() {return Patron.regular("Name", "name@email.com");}

  public Patron buildDefaultResearcher() {return Patron.researcher("Name", "name@email.com");}

  public Patron build(UUID id, PatronType type, String name, String email) {return new Patron(id, type, name, email);}
}
