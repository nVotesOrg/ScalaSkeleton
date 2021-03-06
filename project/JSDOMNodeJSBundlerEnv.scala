import java.io.File

import org.scalajs.jsenv._
import org.scalajs.core.tools.io._
import org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv
import org.scalajs.core.tools.jsdep.ResolvedJSDependency
import org.scalajs.core.tools.io.VirtualJSFile

class JSDOMNodeJSBundlerEnv(
  config: JSDOMNodeJSEnv.Config = JSDOMNodeJSEnv.Config(),
  initFiles: Seq[String] = Seq.empty
)
extends JSDOMNodeJSEnv(config)
{
  def bundlerInitFiles(): Seq[ResolvedJSDependency] =
    initFiles.map(
      (x: String) =>
      org.scalajs.core.tools.jsdep.ResolvedJSDependency.minimal(
        FileVirtualJSFile(new File(x))
      )
    )

  private def bundledLibs(libs: Seq[ResolvedJSDependency]):
  Seq[ResolvedJSDependency] = bundlerInitFiles() ++ libs.map(
    (x: ResolvedJSDependency) =>
    org.scalajs.core.tools.jsdep.ResolvedJSDependency.minimal(
      FileVirtualJSFile(
        new File(
          x.lib.path.replace("fastopt.js", "fastopt-bundle.js")
        )
      )
    )
  )

  override def jsRunner(libs: Seq[ResolvedJSDependency], code: VirtualJSFile): JSRunner =
    super.jsRunner(bundledLibs(libs), code)

  override def asyncRunner(libs: Seq[ResolvedJSDependency], code: VirtualJSFile):
    AsyncJSRunner = super.asyncRunner(bundledLibs(libs), code)

  override def comRunner(libs: Seq[ResolvedJSDependency], code: VirtualJSFile):
    ComJSRunner = super.comRunner(bundledLibs(libs), code)
}

