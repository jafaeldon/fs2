package fs2

import fs2.async.mutable.Topic

package object async {

  /**
   * Creates a new continuous signal which may be controlled asynchronously,
   * and immediately sets the value to `initialValue`.
   */
  def signalOf[F[_]:Async,A](initialValue: A): F[mutable.Signal[F,A]] =
    mutable.Signal(initialValue)

  /** Create a `[[mutable.Semaphore]]`, initialized to the given count. */
  def semaphore[F[_]:Async](initialCount: Long): F[mutable.Semaphore[F]] =
    mutable.Semaphore(initialCount)

  /** Defined as `[[hold]](None, source.map(Some(_)))` */
  def holdOption[F[_]:Async,A](source: Stream[F, A]): Stream[F, immutable.Signal[F,Option[A]]] =
     immutable.Signal.holdOption(source)

  /** Create an unbounded asynchronous queue. See [[mutable.Queue]] for more documentation. */
  def unboundedQueue[F[_]:Async,A]: F[mutable.Queue[F,A]] =
    mutable.Queue.unbounded[F,A]

  /**
   * Create an bounded asynchronous queue. Calls to `enqueue1` will wait until the
   * queue's size is less than `maxSize`. See [[mutable.Queue]] for more documentation.
   */
  def boundedQueue[F[_]:Async,A](maxSize: Int): F[mutable.Queue[F,A]] =
    mutable.Queue.bounded[F,A](maxSize)

  /**
   * A synchronous queue always has size 0. Any calls to `enqueue1` block
   * until there is an offsetting call to `dequeue1`. Any calls to `dequeue1`
   * block until there is an offsetting call to `enqueue1`.
   */
  def synchronousQueue[F[_],A](implicit F: Async[F]): F[mutable.Queue[F,A]] =
    mutable.Queue.synchronous[F,A]

  /**
   * Converts a discrete stream to a signal. Returns a single-element stream.
   *
   * Resulting signal is initially `initial`, and is updated with latest value
   * produced by `source`. If `source` is empty, the resulting signal will always
   * be `initial`.
   *
   * @param source   discrete stream publishing values to this signal
   */
  def hold[F[_]:Async,A](initial: A, source: Stream[F, A]): Stream[F, immutable.Signal[F,A]] =
     immutable.Signal.hold(initial, source)

  /**
    * Creates asynchronous topic, that allows distribute published `A` to arbitrary number of subscribers.
    * Each subscriber is guaranteed to received at least initial `A` passed, or last value published by any publisher.
    */
  def topic[F[_]:Async, A](initial: A):F[Topic[F,A]] =
    Topic(initial)
}
