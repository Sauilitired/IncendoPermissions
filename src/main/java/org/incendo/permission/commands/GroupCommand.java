//
// MIT License
//
// Copyright (c) 2019 Alexander Söderberg
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

package org.incendo.permission.commands;

import com.intellectualsites.commands.Command;
import com.intellectualsites.commands.CommandDeclaration;
import com.intellectualsites.commands.CommandInstance;
import org.incendo.permission.Permissions;
import org.incendo.permission.commands.group.Add;
import org.incendo.permission.commands.group.Create;
import org.incendo.permission.commands.group.Remove;
import org.jetbrains.annotations.NotNull;

@CommandDeclaration(command = "group", aliases = "g", usage = "/prms group", permission = "incendoperms.use.group")
public class GroupCommand extends Command {

    private final Permissions permissions;

    GroupCommand(@NotNull final Permissions permissions) {
        this.permissions = permissions;
        this.createCommand(new Create(permissions));
        this.createCommand(new Add(permissions));
        this.createCommand(new Remove(permissions));
    }

    @Override public boolean onCommand(@NotNull final CommandInstance instance) {
        if (instance.getArguments().length > 0) {
            this.permissions.getResultConsumer().accept(this.handle(instance.getCaller(), instance.getArguments()));
        } else {
            // TODO: Send help menu
            instance.getCaller().message("this is supposed to be a help menu");
        }
        return true;
    }
}
