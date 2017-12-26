package com.roboxue.niffler.execution

import com.roboxue.niffler.Token
import org.jgrapht.GraphPath
import org.jgrapht.alg.shortestpath.AllDirectedPaths
import org.jgrapht.graph.DefaultEdge

import scala.collection.JavaConversions._
import scala.language.existentials

/**
  * @author rxue
  * @since 12/19/17.
  */
case class NifflerEvaluationException(snapshot: ExecutionSnapshot,
                                      tokenWithException: Token[_],
                                      stats: TokenEvaluationStats,
                                      exception: Throwable)
    extends Exception {
  setStackTrace(exception.getStackTrace)

  def getPaths: Seq[GraphPath[Token[_], DefaultEdge]] = {
    new AllDirectedPaths(snapshot.logic.topology)
      .getAllPaths(tokenWithException, snapshot.tokenToEvaluate, true, null)
      .toSeq
  }

  override def getMessage: String = {
    snapshot.tokenToEvaluate.debugString
  }

  override def toString: String = {
    s"""when evaluating NifflerToken ${snapshot.tokenToEvaluate.debugString} in logic ${snapshot.logic.name}
       |Caused by ${tokenWithException.debugString}:
       |${exception.toString}""".stripMargin
  }
}
