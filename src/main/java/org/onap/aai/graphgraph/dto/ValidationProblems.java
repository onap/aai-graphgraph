package org.onap.aai.graphgraph.dto;

import java.util.LinkedList;
import java.util.List;

public class ValidationProblems {
    private List<String> problems = new LinkedList<>();

  public List<String> getProblems() {
    return problems;
  }

  public void setProblems(List<String> problems) {
    this.problems = problems;
  }

  public void addProblem(String problem) {
    problems.add(problem);
  }
}
